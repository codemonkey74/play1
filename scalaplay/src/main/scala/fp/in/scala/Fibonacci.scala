package fp.in.scala

object Fibonacci extends App {

    import scala.annotation.tailrec

    def fib(n: Int) : Int = {
        @tailrec
        def loop(i: Int, a: Int = 0, b: Int = 1) : Int = i match {
            case 1 => b
            case 0 => a
            case _ => loop(i - 1, b, b + a)
        }

        if (n < 0) 0 else loop(n)
    }

    for (i <- 0 to 10) println(i + " -> " + fib(i))
}
