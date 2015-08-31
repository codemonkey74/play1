package com.factors

trait Printer {
  def print(str: String) = println(str)
  def printFactors(): Unit
}
