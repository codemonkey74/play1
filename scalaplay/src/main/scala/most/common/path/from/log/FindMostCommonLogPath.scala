package most.common.path.from.log

object FindMostCommonLogPath extends App with PagesCvt {

    import scala.io.Source

    //1. get records from file
    (for { lines <- Source.fromURL(this.getClass.getResource("/input2_loadtimes.txt")).getLines()
           a = lines.split(",")
           cl = Client(a(1))
           pg = Page(a(2))
    } yield (cl, pg))
    //2. build users paths
    .foldRight(Map.empty[Client, Seq[Page]]) {
      case ((cl, pg), m) if m.isDefinedAt(cl) => m + (cl -> (m(cl) :+ pg))
      case ((cl, pg), m) => m + (cl -> Seq(pg))
    }//3. gen 3 steps paths
    .flatMap {
      case (_, seq) if seq.length > 2 => for {
        i <- 0 to seq.length - 3
        r = seq.slice(i, i + 3)
      } yield PageSeqToPages(r.reverse)
    }
    //4. Histogram of 3 step paths
    .groupBy(identity).mapValues(_.size)
    //5. Filter Max
    .foldRight((Seq.empty[Pages], 0)) {
      case ((p, count), (s, max)) if count < max => (s, max)
      case ((p, count), (s, max)) if count == max => (s :+ p, max)
      case ((p, count), (s, max)) if count > max => (Seq(p), count)
    }
    //6. output
    match {
      case (s: Seq[Pages], max: Int) => s.foreach(p => println(p + " -> " + max))
    }
}
