package most.common.path.from.log

object Run extends App with PagesCvt {

    import scala.io.Source

    def partit3(l: Seq[Page]): Seq[Pages] = {
        (for {i <- 0 to l.length
        paths = l.slice(i, i + 3)
        if paths.length == 3
      } yield PageSeqToPages(paths.reverse)).toList
    }

    //1. get records from file
    val logrecs = for { lines <- Source.fromURL(this.getClass.getResource("/input2_loadtimes.txt")).getLines()
           a = lines.split(",")
           cl = Client(a(1))
           pg = Page(a(2))
    } yield (cl, pg)

    //3. generate consumer path
    val consumerpaths = logrecs.foldRight(Map.empty[Client, Seq[Page]]) {
      case ((cl, pg), m) if m.isDefinedAt(cl) => m + (cl -> (m(cl) :+ pg))
      case ((cl, pg), m) => m + (cl -> Seq(pg))
    }

    //4. for each path list, generate counter (Map)
    val paths = consumerpaths.flatMap {
      case (_, seq) => partit3(seq)
    }

    val counters = paths.foldLeft(Map.empty[Pages, Int]) {
      case (m, path) if m.isDefinedAt(path) => m + (path -> (m(path) + 1))
      case (m, path) => m + (path -> 1)
    }

    //5. find max of Map print path(s).
    val winner = counters.valuesIterator.max
    counters.foreach{
      case (path, count: Int) if count == winner => println(path + " -> " + count)
      case _ => //noop
    }
}
