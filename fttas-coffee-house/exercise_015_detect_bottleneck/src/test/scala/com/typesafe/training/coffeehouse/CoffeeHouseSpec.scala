/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.actor.ActorDSL._
import akka.testkit.{ EventFilter, TestProbe }
import scala.concurrent.duration.DurationInt

class CoffeeHouseSpec extends BaseAkkaSpec {

  "Creating CoffeeHouse" should {
    "result in logging a status message at debug" in {
      EventFilter.debug(pattern = ".*[Oo]pen.*", occurrences = 1) intercept {
        system.actorOf(CoffeeHouse.props(Int.MaxValue))
      }
    }
    "result in creating a child actor with the name 'barista'" in {
      system.actorOf(CoffeeHouse.props(Int.MaxValue), "create-barista")
      TestProbe().expectActor("/user/create-barista/barista")
    }
    "result in creating a child actor with the name 'waiter'" in {
      system.actorOf(CoffeeHouse.props(Int.MaxValue), "create-waiter")
      TestProbe().expectActor("/user/create-waiter/waiter")
    }
  }

  "Sending CreateGuest to CoffeeHouse" should {
    "result in creating a Guest" in {
      val coffeeHouse = system.actorOf(CoffeeHouse.props(Int.MaxValue), "create-guest")
      coffeeHouse ! CoffeeHouse.CreateGuest(Coffee.Akkaccino, Int.MaxValue)
      TestProbe().expectActor("/user/create-guest/$*")
    }
  }
  "Sending ApproveCoffee to CoffeeHouse" should {
    "result in forwarding PrepareCoffee to Barista if caffeineLimit not yet reached" in {
      val barista = TestProbe()
      val coffeeHouse =
        actor(new CoffeeHouse(Int.MaxValue) {
          override def createBarista() = barista.ref
        })
      coffeeHouse ! CoffeeHouse.ApproveCoffee(Coffee.Akkaccino, system.deadLetters)
      barista.expectMsg(Barista.PrepareCoffee(Coffee.Akkaccino, system.deadLetters))
    }
    "result in logging a status message at info if caffeineLimit reached" in {
      EventFilter.info(pattern = ".*[Ss]orry.*", occurrences = 1) intercept {
        val guest = actor(new Act {})
        val coffeeHouse = system.actorOf(CoffeeHouse.props(0))
        coffeeHouse ! CoffeeHouse.ApproveCoffee(Coffee.Akkaccino, guest)
      }
    }
    "result in stopping the Guest if caffeineLimit reached" in {
      val probe = TestProbe()
      val guest = actor(new Act {})
      probe.watch(guest)
      val coffeeHouse = system.actorOf(CoffeeHouse.props(0))
      coffeeHouse ! CoffeeHouse.ApproveCoffee(Coffee.Akkaccino, guest)
      probe.expectTerminated(guest)
    }
  }

  "On termination of Guest, CoffeeHouse" should {
    "remove the guest from the caffeineLimit bookkeping" in {
      val barista = TestProbe()
      val coffeeHouse =
        actor(new CoffeeHouse(Int.MaxValue) {
          override def createBarista() = barista.ref
        })
      coffeeHouse ! CoffeeHouse.CreateGuest(Coffee.Akkaccino, Int.MaxValue)
      val guest = barista.expectMsgPF() {
        case Barista.PrepareCoffee(Coffee.Akkaccino, guest) => guest
      }
      barista.watch(guest)
      system.stop(guest)
      barista.expectTerminated(guest)
      barista.within(2 seconds) {
        barista.awaitAssert {
          coffeeHouse ! CoffeeHouse.ApproveCoffee(Coffee.Akkaccino, guest)
          barista.expectMsgPF(100 milliseconds) {
            case Barista.PrepareCoffee(Coffee.Akkaccino, `guest`) => ()
          }
        }
      }
    }
  }

  "On failure of Guest CoffeeHouse" should {
    "stop it" in {
      val barista = TestProbe()
      val coffeeHouse =
        actor(new CoffeeHouse(Int.MaxValue) {
          override def createBarista() = barista.ref
        })
      coffeeHouse ! CoffeeHouse.CreateGuest(Coffee.Akkaccino, 0)
      val guest = barista.expectMsgPF() {
        case Barista.PrepareCoffee(Coffee.Akkaccino, guest) => guest
      }
      barista.watch(guest)
      guest ! Waiter.CoffeeServed(Coffee.Akkaccino)
      barista.expectTerminated(guest)
    }
  }

  "On failure of Waiter CoffeeHouse" should {
    "restart it and resend PrepareCoffee to Barista" in {
      val barista = TestProbe()
      actor("resend-prepare-coffee")(new CoffeeHouse(Int.MaxValue) {
        override def createBarista() = barista.ref
        override def createWaiter() =
          actor(context, "waiter")(new Act {
            become { case _ => throw Waiter.FrustratedException(Coffee.Akkaccino, system.deadLetters) }
          })
      })
      val waiter = TestProbe().expectActor("/user/resend-prepare-coffee/waiter")
      waiter ! "blow-up"
      barista.expectMsg(Barista.PrepareCoffee(Coffee.Akkaccino, system.deadLetters))
    }
  }
}
