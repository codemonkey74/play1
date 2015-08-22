import scala.concurrent._
import ExecutionContext.Implicits.global

object Main extends App {
  import scala.concurrent._
  import scala.concurrent.duration._

  val ss = List("a1", "b2", "C3")

  def r(s: String) : Future[String] = Future {
    val limit = 2
    for (i <- 1 to limit) yield {
      println(String.format("r(%s) waiting %s more seconds...",
        s, (limit - i).toString))
      Thread.sleep(1000)

    }
    s.reverse
  }

  val f_revs = ss.map { r(_) }

  println("Look ma, no blocking!")

  val rev = f_revs.par.map { Await.result(_, Duration.Inf) }.mkString(" ")

  println(rev)
}
