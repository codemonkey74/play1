package priority.queue

trait Leaderboard {
    def setPoints(user: String, points: Int) // time complexity < O(n)
    def getPoints(user: String): Option[Int] // time complexity < O(n)
    def topk(k: Int): Seq[String] // time complexity <= O(k)
}

class Leaderboarder (topCapacity: Int = 3) extends Leaderboard {

    import scala.collection.SortedMap
    import scala.collection.immutable.Set

    case class Data(name: String, score: Int)
    object Data {
        def apply(d: Data, points: Int): Data = new Data(d.name, d.score + points)
    }
    val o = Ordering.by((k: Int) => -k)
    var ctr = 0
    var pq = SortedMap[Int, Set[String]]()(o)
    var mp = Map.empty[String, Data]

    def updateScore(user: String, points: Int): Data = {
        val nextData: Data =  (user, points) match {
            case (u, p) if mp.isDefinedAt(u) => Data(mp(u), p)
            case _ => Data(user, points)
        }
        mp += (user -> nextData)
        mp.get(user).get
    }

    def removeFromPQ(od: Option[Data]) = {
        (for {
            d <- od
            l <- pq.get(-d.score)
        } yield -d.score -> (l - d.name)).foreach {
            case (a, ls) if ls.nonEmpty => ctr -= 1; pq += (a -> ls)
            case (a, ls) => ctr -= 1; pq = pq - a
        }
    }
    def removeFromHead = {
        pq.headOption.foreach {
            case (i: Int, l) if l.headOption.nonEmpty => removeFromPQ(Some(Data(l.head, -i)))
            case _ => //noop
        }
    }

    def insertOnPQ(ndo: Option[Data]): Unit = ndo match {
        case Some(nd: Data) => {
            val s: Set[String] = pq.getOrElse(-nd.score, Set[String]())
            pq += ((-nd.score) -> (s + nd.name))
            ctr += 1
        }
        case _ => //noop
    }


    def updatePQ(od: Option[Data], nd: Data) = {
        removeFromPQ(od)
        (nd, pq.size < topCapacity, pq.nonEmpty && nd.score > -pq.head._1) match {
            case (n: Data, true, _) => insertOnPQ(Some(nd))
            case (n: Data, false, true) => {
                removeFromHead
                insertOnPQ(Some(nd))
            }
            case _ => //noop (less than minimum).
        }
    }

    override def setPoints(user: String, points: Int): Unit = {
        val dprev = mp.get(user)
        val dnext = updateScore(user, points)
        updatePQ(dprev, dnext)
    }

    // time complexity < O(n)
    override def topk(k: Int): Seq[String] = pq.foldRight(Seq.empty[String]) {
        case ((_, l), s) if s.isEmpty => l.toSeq
        case ((_, l), s) => println(s); s ++ l.toSeq
        case (_, s) => s
    }.take(k)

    // time complexity < O(n)
    override def getPoints(user: String): Option[Int] = mp.get(user).map(_.score)
}

object LeaderBoardTest extends App {
    val l = new Leaderboarder(6)
    l.setPoints("a", 2)
    println("2 =" + l.getPoints("a").getOrElse(0))
    l.setPoints("a", 3)
    println("5 =" + l.getPoints("a").getOrElse(0))
    l.setPoints("b", 5)
    println("leaders " + l.topk(2).mkString(", "))
    l.setPoints("b", 6)
    println("11 =" + l.getPoints("b").getOrElse(0))
    l.setPoints("c", 7)
    println("7 =" + l.getPoints("c").getOrElse(0))
    l.setPoints("d", 8)
    l.setPoints("e", 9)
    l.setPoints("f", 10)
    println("leaders " + l.topk(2).mkString(", "))
    println(l.mp)
    println(l.pq)

}


