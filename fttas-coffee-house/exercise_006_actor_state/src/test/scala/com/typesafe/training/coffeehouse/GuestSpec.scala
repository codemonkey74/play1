/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.{ EventFilter, TestProbe }

class GuestSpec extends BaseAkkaSpec {

  "Sending CoffeeServed to Guest" should {
    "result in increasing the coffeeCount and log a status message at info" in {
      val guest = system.actorOf(Guest.props(system.deadLetters, Coffee.Akkaccino))
      EventFilter.info(pattern = """.*[Ee]njoy.*1\..*""", occurrences = 1) intercept {
        guest ! Waiter.CoffeeServed(Coffee.Akkaccino)
      }
    }
  }

  "Sending CoffeeFinished to Guest" should {
    "result in sending ServeCoffee to Waiter" in {
      val waiter = TestProbe()
      val guest = system.actorOf(Guest.props(waiter.ref, Coffee.Akkaccino))
      guest ! Guest.CoffeeFinished
      waiter.expectMsg(Waiter.ServeCoffee(Coffee.Akkaccino))
    }
  }
}

