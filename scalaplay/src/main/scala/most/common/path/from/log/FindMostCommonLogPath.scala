package most.common.path.from.log

import scala.most.common.path.from.log.Time

object FindMostCommonLogPath extends App {

  def threePath(logFileName: String) = {
    import scala.io.Source
    (for {
    lines <- Source.fromURL(this.getClass.getResource(logFileName)).getLines()
      a = lines.split(",")
      cl = Client(a(1))
      pg = PageTime(a(2), Time(("0" + a(3).filter(_.isDigit)).toInt))
    } yield (cl, pg))
      //2. build users paths
      .toSeq.groupBy(_._1)
      //3. gen 3 steps paths
      .flatMap {
        case (cl, seq) if seq.length > 2 => for {
            i <- 0 to seq.length - 3
            r = seq.map(x => x._2).slice(i, i + 3)
          } yield Some(Pages(r), cl)
        case _ => None
      }.flatten
  }



  //1. get records from file
  val f = threePath("/input0_loadtimes.txt")

  //2. Get all Step Paths.
  val x = f
  //3. Strip clients
    .map(_._1)
  //3. Histogram of 3 step paths
    .groupBy(_.asPath).mapValues(_.size)
  //4. Max paths
  println(x.maxBy(_._2)._1)

  //2. Slowest PATH.
  val y = f
  //3. Max time
  val maxY = y.maxBy(_._1.time.t)._1
  //4. all clients
  val clY = y.filter(_._1.asPath == maxY.asPath).map(_._2)
  //5. output
  println(s"${maxY.asPath}, ${maxY.time}, ${clY.mkString("[", ",", "]")}")
}
