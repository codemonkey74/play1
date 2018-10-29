package com.factors

import scala.util.Try

object PrintFactors extends App {
  def getNatural(args: Array[String]): Option[Long] = {
    Try { args(0).toLong }.toOption
  }

  getNatural(args) match {
    case None => System.err.println("Need one Positive integer to Print its factors.")
    case Some(n) if n <= 0 => System.err.println("Need one Positive integer to Print its factors.")
    case Some(n) => Factors(n).printFactors()
  }
}
