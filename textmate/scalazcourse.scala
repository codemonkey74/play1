// -1. Algebraic type

A is a B or C (sum type (IS))
B has a D and E  (prod type (HAS))
C has a F and G 

sealed trait A {}
final case class B(d: D, e: E) extends A
final case class C(f: F, g: G) extends A

// 0. Structural recursion

// Fold pattern:

sealed trait A {
	def foo: H = {
		this match {
			case B(d, e) = doB(d, e)
			case C(f, g) = doC(f, g)
		}
	}
}
final case class B(d: D, e: E) extends A
final case class C(f: F, g: G) extends A

  // the recursion is if D extends A, p. ex.
	
// 1. Type Classes

 // ad hoc polimorphism 

import scalaz.Monoid

// 1.2. Type Class instances

import scalaz.std.list._    //monoid on lists.
import scalaz.std.string._  //monoid on strings.

// 1.3. Type Class interface

Monoid[List[String]].append(
	List("a"), List("b")
)

// 1.4. 

import scalaz.syntax.monoid._

List("a") |+| List("b")

//is equivalent to
Monoid[List[String]].append(
	List("a"), List("b")
)

// 2.   Monoids

// Motivation: MapReduce
//   Need to guarantee if order of results matter. (Associativity)
//   Need to guarantee the operation keeps on the same Universe (Int => Int, etc...)
//   Need to gurantee the existence of an (Identity)

// 3. Functors
val b = Option[A]
val c = Future[A]
val d = Seq[A]


//functor is this:  A type F[A] that has a "map" operation with type ( A => B ) => F[B]
// F[A] map A => B = F[B]

sealed trait Result [A] {
	def map[B](f: A => B): Result[B] = 
	  this match {
		  case Success(value) => f(value)
		  case Failure(f) => Failure(f)
	  }
}

final case class Success[A](value: A) extends Result[A]
final case class Failure[A](error: String) extends Result[A]

// 4 Monads


// monad is : A type F[A] that has a flatMap operation with type (F[A], A => B) => F[B]

sealed trait Result [A] {
	def map[B](f: A => B): Result[B] = 
	  this match {
		  case Success(value) => Success(f(value))
		  case Failure(error) => Failure(error)
	  }

	def flatMap[B](f: A => Result[B]): Result[B] = 
	  this match {
		  case Success(value) => f(value)
		  case Failure(error) => Failure(error)
	  }
}

object Result {
		def point[A](a: A): Result[A] = Success(a)
}

final case class Success[A](value: A) extends Result[A]
final case class Failure[A](error: String) extends Result[A]





sealed trait Result[A]
final case class Success[A](value: A) extends Result[A]
final case class Failure[A](error: String) extends Result[A]

object Result {
		def point[A](a: A): Result[A] = Success(a)
}


// 5 Applicatives

//   5.1 HigherKind types

// Int is a type
// List[Int] is a type
// List[A] is a type constructor or function that takes a type and generates a type:  List(A) => List[A]
// List[_]   ->  type => List[type]

import scalaz.Monad
object forHighKinder {
	import scala.language.HigherKinds
	
	def incrementVisits[F[_]: Monad](v: F[Int]): F[Int] =
	    v.map(_ + 1)
}

3.incrementVisits(scalaz.Option) //Some(4) 



// applicatives does some sort of zipping, but all at the same time.
//  F[A] f(a:A, b: A) =>  F[A]
(Success(1) |@| Success(2)){ _ + _ }   //Success(3)
(Success(1) |@| Success(2) |@| Success(3)){ _ + _ + _}   //Success(6)
(Success(1) |@| Failure("")) //Failure("")



// 6 Natural function
///   F[_] ~> G[_]
