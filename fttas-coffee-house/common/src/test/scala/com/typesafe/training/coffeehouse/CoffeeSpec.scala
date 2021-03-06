/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

class CoffeeSpec extends BaseSpec {

  import Coffee._

  "coffees" should {
    "contain Akkaccino, MochaPlay and PinaScalada" in {
      beverages should ===(Set[Coffee](Akkaccino, MochaPlay, CaffeScala))
    }
  }

  "Calling apply" should {
    "create the correct Beverage for a known code" in {
      apply("A") should ===(Akkaccino)
      apply("a") should ===(Akkaccino)
      apply("M") should ===(MochaPlay)
      apply("m") should ===(MochaPlay)
      apply("C") should ===(CaffeScala)
      apply("c") should ===(CaffeScala)
    }
    "throw an IllegalArgumentException for an unknown code" in {
      an[IllegalArgumentException] should be thrownBy apply("1")
    }
  }

  "Calling anyOther" should {
    "return an other Coffee than the given one" in {
      forAll(beverages) { coffee => anyOther(coffee) should !==(coffee) }
    }
  }
}
