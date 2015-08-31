package com.factors.test

import com.factors.{Printer, Factors}
import org.scalatest.{Matchers, FlatSpec}

class FactorsTest extends FlatSpec with Matchers {
  trait Acc extends Printer{
    var accumulator: Seq[String] = Seq.empty[String]

    override def print(str: String): Unit = {
      accumulator = accumulator :+ str
      ()
    }

    def run() : Seq[String] = {
      printFactors()
      accumulator.toList
    }
  }
  class FactorMock(l: Long) extends Factors(l) with Acc;
  object FactorMock {
    def apply(l: Long) = new FactorMock(l)
  }

  "A Factors" should "produce the next divisor as empty for no dividers" in {
    Factors(2L).allDivisors should be (List.empty[Long])
    Factors(3L).allDivisors should be (List.empty[Long])
    Factors(13L).allDivisors should be (List.empty[Long])
  }

  it should "produce some divisors" in {
    Factors(12L).allDivisors should be (List(2, 3, 4, 6).reverse)
    Factors(16L).allDivisors should be (List(2, 4, 8).reverse)
    Factors(32L).allDivisors should be (List(2, 4, 8, 16).reverse)
    Factors(64L).allDivisors should be (List(2, 4, 8, 16, 32).reverse)
    Factors(49L).allDivisors should be (List(7).reverse)
  }

  it should "send a list of divisors" in {
    FactorMock(6L).run().mkString(", ") should be("6 * 1, 3 * 2")
    FactorMock(32L).run().mkString(", ") should be("32 * 1, 16 * 2, 8 * 4, 8 * 2 * 2, 4 * 4 * 2, 4 * 2 * 2 * 2, 2 * 2 * 2 * 2 * 2")
    FactorMock(96L).run().mkString(", ") should be("96 * 1, 48 * 2, 32 * 3, 24 * 4, 24 * 2 * 2, 16 * 6, 16 * 3 * 2, 12 * 8, 12 * 4 * 2, 12 * 2 * 2 * 2, 8 * 6 * 2, 8 * 4 * 3, 8 * 3 * 2 * 2, 6 * 4 * 4, 6 * 4 * 2 * 2, 6 * 2 * 2 * 2 * 2, 4 * 4 * 3 * 2, 4 * 3 * 2 * 2 * 2, 3 * 2 * 2 * 2 * 2 * 2")
    FactorMock(10247L).run().mkString(", ") should be("10247 * 1")
    FactorMock(64L).run().mkString(", ") should be("64 * 1, 32 * 2, 16 * 4, 16 * 2 * 2, 8 * 8, 8 * 4 * 2, 8 * 2 * 2 * 2, 4 * 4 * 4, 4 * 4 * 2 * 2, 4 * 2 * 2 * 2 * 2, 2 * 2 * 2 * 2 * 2 * 2")
  }
}
