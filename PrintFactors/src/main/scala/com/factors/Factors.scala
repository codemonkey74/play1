package com.factors

import scala.annotation.tailrec

class Factors(n: Long) extends Printer {

  /**
   * Get all numbers that n divides by and return a list with all of them.
   * @param number is the number to find factors
   * @param factor is the current factor attempt.
   * @param factors is the accumulated factors found so far
   * @return a list of all factors (all divisors of) n
   */
  @tailrec
  private[this] def getLowDivisors_(number: Long, factor: Long = 2L, factors: List[Long] = Nil): List[Long] = factor * factor <= number match {
    case true if number % factor == 0 => getLowDivisors_(number, factor + 1, factor :: factors) //found more
    case true => getLowDivisors_(number, factor + 1, factors) //try next number
    case false => factors //done.
  }

  /**
   * Keeps all divisors on an immutable List.
   */
  lazy val lowDivisors: List[Long] =
    getLowDivisors_(n)

  /**
   * Aggregate lowDivisors to fulfill the array with all of them.
   */
  lazy val allDivisors: List[Long] = 
    lowDivisors.filterNot(d => d * d == n).map(n / _).reverse ++ lowDivisors

  /**
   * Go through the factors domain list to find which ones are factors of number. Accumulate factors that apply and
   *  recurse until completely factoring number, then print out the accumulator. If not fully factored. drops branch.
   * @param number the number to be refactored
   * @param domain the domain list of factors to run through in decrescent order.
   * @param acc the current path of factoring.
   */
  def findFactorsRec(number: Long, domain: List[Long], acc: List[Long] = Nil) : Unit = {
    domain.foreach { maxdiv =>
      (number % maxdiv, number / maxdiv) match {
        case (m, nextn) if m > 0 || nextn == 0L => //noop. not a divisor or too big
        case (0L, nextn) if nextn == 1L => print ((maxdiv :: acc).reverse.mkString(" * ")) //valid leaf
        case (0L, nextn) => findFactorsRec(nextn, domain.dropWhile(_ > maxdiv), maxdiv :: acc) //keep traversing
      }
    }
  }

  def printFactors(): Unit = n match {
    case n if n > 0 =>
      print (n.toString + " * 1")
      findFactorsRec(n, allDivisors)
    case _ => //noop
  }
}

object Factors {
  def apply(n: Long) = new Factors(n)
}