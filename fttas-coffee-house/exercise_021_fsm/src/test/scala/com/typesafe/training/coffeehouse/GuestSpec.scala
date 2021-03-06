/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.testkit.{ EventFilter, TestProbe }
import scala.concurrent.duration.DurationInt

class GuestSpec extends BaseAkkaSpec {

  "Sending CoffeeServed to Guest" should {
    "result in increasing the coffeeCount and log a status message at info" in {
      val guest = system.actorOf(Guest.props(system.deadLetters, Coffee.Akkaccino, 100 milliseconds, Int.MaxValue))
      EventFilter.info(pattern = """.*[Ee]njoy.*1\..*""", occurrences = 1) intercept {
        guest ! Waiter.CoffeeServed(Coffee.Akkaccino)
      }
    }
    "result in sending ServeCoffee to Waiter after finishCoffeeDuration" in {
      val waiter = TestProbe()
      val guest = createGuest(waiter)
      waiter.within(50 milliseconds, 200 milliseconds) { // The timer is not extremely accurate, relax the timing constraints.
        guest ! Waiter.CoffeeServed(Coffee.Akkaccino)
        waiter.expectMsg(Waiter.ServeCoffee(Coffee.Akkaccino))
      }
    }
    "result in sending Complaint to Waiter for a wrong coffee" in {
      val waiter = TestProbe()
      val guest = createGuest(waiter)
      guest ! Waiter.CoffeeServed(Coffee.MochaPlay)
      waiter.expectMsg(Waiter.Complaint(Coffee.Akkaccino))
    }
  }

  def createGuest(waiter: TestProbe) = {
    val guest = system.actorOf(Guest.props(waiter.ref, Coffee.Akkaccino, 100 milliseconds, Int.MaxValue))
    waiter.expectMsg(Waiter.ServeCoffee(Coffee.Akkaccino)) // Creating Guest immediately sends Waiter.ServeCoffee
    guest
  }
}
