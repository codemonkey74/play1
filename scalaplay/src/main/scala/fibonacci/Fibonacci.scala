package fibonacci

object Fibonacci extends App {

  def fibFrom(a: BigInt = 0, b: BigInt = 1): Stream[BigInt] = a #:: fibFrom(b, a + b)
  val fib = fibFrom()

  println(fib.take(1000).last)
  fib.slice(1, 30).foreach(println)
}
