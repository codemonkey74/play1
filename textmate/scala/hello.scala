case class A[T](v: T)
case class B[T, S](v: T, s: S)
	
object A {
	implicit def atoT[T](a: A[T]): T = a.v
}

object B {
	implicit def btoT[T, S](b: B[T,S]): T = b.v
}

object Add {
	implicit def add[T](a: A[T], b: B[T,T]): Unit =
		println("add called")
	implicit def add[T, S](a: A[T], b: B[T,S]): Unit =
			println("add TS called")
    implicit def add(a: Int, b: Int): Unit =
		println("add Int called")	    
}

import Add._
import A._
import B._

val a = new A(1)
val b = new B(1,2)

add(a,b)
