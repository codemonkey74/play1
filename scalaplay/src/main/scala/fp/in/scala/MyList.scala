package fp.in.scala

sealed trait MyList[+A]
case object MyNil extends MyList[Nothing]
case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

object MyList {

    import scala.annotation.tailrec

    @tailrec
    def sum(ints: MyList[Int], acc: Int = 0): Int = ints match {
        case MyNil => acc
        case Cons(h, tail) => sum(tail, h + acc)
    }

    @tailrec
    def product(doubles: MyList[Double], acc: Double = 1.0): Double = doubles match {
        case MyNil => acc
        case Cons(0, _) => 0.0
        case Cons(h, tail) => product(tail, h * acc)
    }

    def apply[A](as: A*): MyList[A] =
        if (as.isEmpty) MyNil
        else Cons(as.head, apply(as.tail: _*))

    def tail[A](list: MyList[A]): MyList[A] = list match {
        case MyNil => list
        case Cons(h, tail) => tail
    }

    def setHead[A](list: MyList[A], head: A) = Cons(head, tail(list))

    @tailrec
    def drop[A](list: MyList[A], n: Int): MyList[A] = (list, n) match {
        case (_, d) if d <= 0 => list
        case (MyNil, _) => list
        case (Cons(h, tail), d) => drop(tail, d - 1)
    }

    @tailrec
    def dropWhile[A](list: MyList[A])(f: A => Boolean): MyList[A] = list match {
        case Cons(h, tail) if f(h) => dropWhile(tail)(f)
        case _ => list
    }

    def append[A](list1: MyList[A], list2: MyList[A]): MyList[A] = list1 match {
        case MyNil => list2
        case Cons(h, tail) => Cons(h, append(tail, list2))
    }

    @tailrec
    def reverse[A](list: MyList[A], acc: MyList[A] = MyNil): MyList[A] = list match {
        case MyNil => acc
        case Cons(h, tail) => reverse(tail, Cons(h, acc))
    }

    def init[A](list: MyList[A]): MyList[A] = reverse(drop(reverse(list), 1))


    def sum2(list: MyList[Int]): Int = foldRight(list)(0)(_ + _)
    def prod2(list: MyList[Double]): Double = foldRight(list)(1.0)(_ * _)

    def length[_](list: MyList[_]): Int = foldRight(list)(0)((_, count) => count + 1)

    //non tailrec
    def foldRight[A, B](list: MyList[A])(z: B)(f: (A, B) => B): B = list match {
        case MyNil => z
        case Cons(h, tail) => f(h, foldRight(tail)(z)(f))
    }

    @tailrec
    def foldLeft[A, B](list: MyList[A])(z: B)(f: (B, A) => B): B = list match {
        case MyNil => z
        case Cons(h, tail) => foldLeft(tail)(f(z, h))(f)
    }

    def sum3(list: MyList[Int]): Int = foldLeft(list)(0)(_ + _)
    def prod3(list: MyList[Double]): Double = foldLeft(list)(1.0)(_ * _)

    def length3[_](list: MyList[_]): Int = foldLeft(list)(0)((count, _) => count + 1)

    def FoldRight3[A, B](list: MyList[A])(z: B)(f: (A, B) => B): B =
        foldLeft(reverse(list))(z)((b: B, a: A) => f(a,b))
}
