package scala.grid.path.counter

case class Position(i: Int, j: Int) {
  override def toString: String = s"$i,$j"
  require(i >= 0 && j >= 0)

  def nextI: Position = this.copy(i = i + 1)
  def nextJ: Position = this.copy(j = j + 1)

  def -(other: Position) = Position(i - other.i, j - other.j)
}

object Position {
  def last(n: Int, m: Int) = Position(n-1, m-1)
}

sealed trait Memoize
case class LinkedTo(p: Position) extends Memoize
case object Blocked extends Memoize
case class Calc(i: BigInt) extends Memoize

object GridPathCounter extends App {
  import scala.collection.mutable.{ Map => MutableMap }


  type Grid = MutableMap[Position, Memoize]

  //Calculates a linked grid throw Newton's binomial
  class Grider() {

    def newtonBin(position: Position): BigInt = {
      val smaller = if (position.i <= position.j) position.i else position.j
      val series = position.i + position.j
      (BigInt(0) to (smaller-1)).foldLeft(BigInt(1)) {
        case (acc, idx) => acc * (series - idx) / (idx + 1)
      }
    }

    def computeBlocks(p: Position, end: Position): BigInt = {
      val factor = newtonBin(end - p)
      val value = newtonBin(p)
      -factor * value
    }

    def computeLinks(p: Position, l: Position, end: Position): BigInt = {
      val factorpe = newtonBin(end - p)
      val curValue = newtonBin(p)
      val factorle = newtonBin(end - l)
      curValue * (factorle - factorpe)
    }

    def calculateTotal(end: Position, anomalies: Grid): Option[BigInt] = {
      val (blocks, links) = anomalies.partition(_._2 == Blocked)

      val blocked: BigInt = blocks.foldLeft(newtonBin(end)) {
        case (acc, (p, b)) => acc + computeBlocks(p, end)
      }

      val blockedCorrected = blocked + (for {
        p1 <- blocks.keys
        p2 <- blocks.keys
        if p1 != p2 && p2.i >= p1.i && p2.j >= p1.j
      } yield -computeLinks(p1, p2, end)).sum

      val linked = links.foldLeft(blockedCorrected) {
        case (acc, (p, LinkedTo(l))) => acc + computeLinks(p, l, end)
      }
      Some(linked)
    }
  }


  val anomalies: Grid = MutableMap.empty[Position, Memoize]

  var testCount = 0
  def test(m1: => Option[BigInt], m2: => Option[BigInt]): Unit = {
    testCount += 1
    System.out.println(s"Running test $testCount")
    assert(m1 == m2, s"$m1 did not match $m2 for test #$testCount")
  }

  test(new Grider().calculateTotal(Position.last(2, 2), anomalies), Some(BigInt(2)))
  test(new Grider().calculateTotal(Position.last(3, 4), anomalies), Some(BigInt(10)))
  test(new Grider().calculateTotal(Position.last(5, 5), anomalies), Some(BigInt(70)))
  test(new Grider().calculateTotal(Position.last(2, 2), anomalies + (Position(0, 1)->Blocked)), Some(BigInt(1)))
  test(new Grider().calculateTotal(Position.last(5, 5), anomalies + (Position(1, 2)->LinkedTo(Position(3, 0)))), Some(BigInt(55)))
  test(new Grider().calculateTotal(Position.last(5, 5), anomalies + (Position(2, 1)->LinkedTo(Position(0, 3)))), Some(BigInt(55)))
  test(new Grider().calculateTotal(Position.last(20, 20), anomalies), Some(BigInt("35345263800")))

  test(new Grider().calculateTotal(Position.last(3, 4), anomalies + (Position(0, 1) -> Blocked)), Some(BigInt(4)))
  test(new Grider().calculateTotal(Position.last(3, 4), anomalies ++ Map(Position(0, 1) -> Blocked, Position(1,1) -> Blocked)), Some(BigInt(1)))
  test(new Grider().calculateTotal(Position.last(1, 4), anomalies + (Position(0, 1) -> Blocked)), Some(BigInt(0)))
  test(new Grider().calculateTotal(Position.last(1, 4), anomalies ++ Map(Position(0, 1) -> Blocked, Position(0, 2) -> Blocked)), Some(BigInt(1)))

  class Grider2() {
    val mem: Grid = MutableMap.empty[Position, Memoize]

    def memOrBorder(p: Position, useMap: Option[Unit] = Some()): Option[Memoize] =
      useMap.flatMap(_ => mem.get(p))
        .orElse( alternative = p match {
          case Position(0, 0) => Some(Calc(1))
          case Position(0, j) => mem.get(Position(0, j - 1)).filterNot(_.isInstanceOf[LinkedTo]).orElse(Some(Blocked))
          case Position(i, 0) => mem.get(Position(i - 1, 0)).filterNot(_.isInstanceOf[LinkedTo]).orElse(Some(Blocked))
          case Position(i, j) =>
            for {
              up <- mem.get(Position(i - 1, j))
              left <- mem.get(Position(i, j - 1))
            } yield (up, left) match {
              case (Calc(u), Calc(l)) => Calc(u + l)
              case (_, l: Calc) => l
              case (u: Calc, _) => u
              case _ => Blocked
            }
        })

    //TODO: This is wrong. need to adjust for links only. not calculate the whole lot.
    def calcFrom(begin: Position, end: Position, useMap: Option[Unit] = Some()): Unit =
      begin.i to end.i foreach { i =>
        begin.j to end.j foreach { j =>
          val p = Position(i, j)
          memOrBorder(p, useMap).map(v => mem += (p -> v))
        }
      }

    def processLinks(end: Position, src: Position, lnk: LinkedTo): Unit = {
       mem.get(lnk.p) match {
         case None => throw new RuntimeException(s"Found link to wrong ${lnk}")
         case Some(Blocked) => mem += (src -> Blocked)
         case Some(dst) =>
           mem -= src
           (memOrBorder(src), dst) match {
             case (Some(Calc(u)), Calc(v)) =>
               mem += lnk.p -> Calc(u + v)
               mem += src -> Blocked
               calcFrom(lnk.p.nextJ, end, None)
               calcFrom(lnk.p.nextI, end, None)
             case (Some(Calc(u)), l2: LinkedTo) =>
               mem += (src -> l2)
               processLinks(end, src, l2)
             case _ => //noop.
           }
       }
    }

    def calculateTotal(end: Position, anomalies: Grid): Option[BigInt] = {
      val ini = Position(0, 0)
      mem ++= anomalies
      calcFrom(Position(0,0), end)

      val links = anomalies.collect {
        case (pos, l: LinkedTo) => pos -> l
      }

      links.foreach{ case (src, lnk) => processLinks(end, src, lnk) }

      mem.get(end).map{
        case Calc(i) => i
        case _ => 0
      }
    }
  }

  test(new Grider2().calculateTotal(Position.last(2, 2), anomalies), Some(BigInt(2)))
  test(new Grider2().calculateTotal(Position.last(3, 4), anomalies), Some(BigInt(10)))
  test(new Grider2().calculateTotal(Position.last(5, 5), anomalies), Some(BigInt(70)))
  test(new Grider2().calculateTotal(Position.last(2, 2), anomalies + (Position(0, 1)->Blocked)), Some(BigInt(1)))
  test(new Grider2().calculateTotal(Position.last(5, 5), anomalies + (Position(1, 2)->LinkedTo(Position(3, 0)))), Some(BigInt(55)))
  test(new Grider2().calculateTotal(Position.last(5, 5), anomalies + (Position(2, 1)->LinkedTo(Position(0, 3)))), Some(BigInt(55)))
  test(new Grider2().calculateTotal(Position.last(20, 20), anomalies), Some(BigInt("35345263800")))

  test(new Grider2().calculateTotal(Position.last(3, 4), anomalies + (Position(0, 1) -> Blocked)), Some(BigInt(4)))
  test(new Grider2().calculateTotal(Position.last(3, 4), anomalies ++ Map(Position(0, 1) -> Blocked, Position(1,1) -> Blocked)), Some(BigInt(1)))
  test(new Grider2().calculateTotal(Position.last(1, 4), anomalies + (Position(0, 1) -> Blocked)), Some(BigInt(0)))


}
