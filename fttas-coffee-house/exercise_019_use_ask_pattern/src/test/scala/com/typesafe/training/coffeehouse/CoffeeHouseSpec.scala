/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.actor.ActorDSL._
import akka.testkit.{ EventFilter, TestProbe }
import scala.concurrent.duration.{ Duration, DurationInt, MILLISECONDS => Millis }

object CoffeeHouseSpec {

  trait BaristaNoRouter {
    this: CoffeeHouse =>

    private val baristaPrepareCoffeeDuration =
      Duration(context.system.settings.config.getDuration("coffee-house.barista.prepare-coffee-duration", Millis), Millis)

    private val baristaAccuracy = context.system.settings.config getInt "coffee-house.barista.accuracy"

    override def createBarista() =
      context.actorOf(Barista.props(baristaPrepareCoffeeDuration, baristaAccuracy), "barista")
  }
}

class CoffeeHouseSpec extends BaseAkkaSpec {

  import CoffeeHouseSpec._

  "Creating CoffeeHouse" should {
    "result in logging a status message at debug" in {
      EventFilter.debug(pattern = ".*[Oo]pen.*", occurrences = 1) intercept {
        actor(new CoffeeHouse(Int.MaxValue) with BaristaNoRouter)
      }
    }
    "result in creating a child actor with the name 'barista'" in {
      actor("create-barista")(new CoffeeHouse(Int.MaxValue) with BaristaNoRouter)
      TestProbe().expectActor("/user/create-barista/barista")
    }
    "result in creating a child actor with the name 'waiter'" in {
      actor("create-waiter")(new CoffeeHouse(Int.MaxValue) with BaristaNoRouter)
      TestProbe().expectActor("/user/create-waiter/waiter")
    }
  }

  "Sending CreateGuest to CoffeeHouse" should {
    "result in creating a Guest" in {
      val coffeeHouse = actor("create-guest")(new CoffeeHouse(Int.MaxValue) with BaristaNoRouter)
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
        val coffeeHouse = actor(new CoffeeHouse(0) with BaristaNoRouter)
        coffeeHouse ! CoffeeHouse.ApproveCoffee(Coffee.Akkaccino, guest)
      }
    }
    "result in stopping the Guest if caffeineLimit reached" in {
      val probe = TestProbe()
      val guest = actor(new Act {})
      probe.watch(guest)
      val coffeeHouse = actor(new CoffeeHouse(0) with BaristaNoRouter)
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

  "Sending GetStatus to CoffeeHouse" should {
    "result in a Status response" in {
      val sender = TestProbe()
      implicit val _ = sender.ref
      val coffeeHouse = actor(new CoffeeHouse(Int.MaxValue) with BaristaNoRouter)
      coffeeHouse ! CoffeeHouse.GetStatus
      sender.expectMsg(CoffeeHouse.Status(0))
    }
  }
}

