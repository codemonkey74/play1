package fp.in.scala

object IsSorted extends App {
    //Excerpt From: Paul Chiusano Rúnar Bjarnason. “Functional Programming in Scala.” iBooks.
    def isSorted[A](as: Array[A], ordered: (A,A) => Boolean): Boolean = {
        import scala.annotation.tailrec
        @tailrec
        def asc(idx: Int = 1): Boolean = (idx >= as.length) || ordered (as(idx), as(idx - 1)) && asc(idx + 1)

        asc()
    }

    def curry[A,B,C](f: (A, B) => C): A => (B => C) = (a) => (b) => f(a, b)
    def uncurry[A,B,C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)
    def compose[A,B,C](f: B => C, g: A => B): A => C = (a) => f(g(a))
    def compose2[A,B,C](f: B => C, g: (A, A) => B): (A, A) => C = (a1, a2) => f(g(a1, a2))

    def intComp = compose2((r: Int) => r >= 0, uncurry((a: Int) => a.compare))

    assert(isSorted(Array(1), intComp))
    assert(isSorted(Array(1,2,3), intComp))
    assert(isSorted(Array(1,2,3,3,4), intComp))
    assert(! isSorted(Array(4,1,2,3), intComp))

    assert( 3 == ( List(1,2,3,4,5) match {
        case ::(x, ::(2, ::(4, _))) => x
        case Nil => 42
        case ::(x, ::(y, ::(3, ::(4, _)))) => x + y
        case ::(h, t) => t.foldLeft(h) ( _ + _ )
        case _ => 101
    }))
}
