/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.typesafe.training.coffeehouse

import akka.actor.ActorDSL._
import akka.testkit.{ EventFilter, TestProbe }
import akka.util.Timeout
import scala.concurrent.duration.DurationInt

class CoffeeHouseAppSpec extends BaseAkkaSpec {

  import CoffeeHouseApp._

  implicit val statusTimeout = 100 milliseconds: Timeout

  "Calling argsToOpts" should {
    "return the correct opts for the given args" in {
      argsToOpts(List("a=1", "b", "-Dc=2")) should ===(Map("a" -> "1", "-Dc" -> "2"))
    }
  }

  "Calling applySystemProperties" should {
    "apply the system properties for the given opts" in {
      System.setProperty("c", "")
      applySystemProperties(Map("a" -> "1", "-Dc" -> "2"))
      System.getProperty("c") should ===("2")
    }
  }

  "Creating CoffeeHouseApp" should {
    "result in creating a top-level actor named 'coffee-house'" in {
      new CoffeeHouseApp(system)
      TestProbe().expectActor("/user/coffee-house")
    }
  }

  "Calling createGuest" should {
    "result in sending CreateGuest to CoffeeHouse count number of times" in {
      val probe = TestProbe()
      new CoffeeHouseApp(system) {
        createGuest(2, Coffee.Akkaccino, Int.MaxValue)
        override def createCoffeeHouse() = probe.ref
      }
      probe.receiveN(2) shouldEqual List.fill(2)(CoffeeHouse.CreateGuest(Coffee.Akkaccino, Int.MaxValue))
    }
  }

  "Calling getStatus" should {
    "result in logging the AskTimeoutException at error for CoffeeHouse not responding" in {
      new CoffeeHouseApp(system) {
        EventFilter.error(pattern = ".*AskTimeoutException.*") intercept status()
        override def createCoffeeHouse() = system.deadLetters
      }
    }
    "result in logging the status at info" in {
      new CoffeeHouseApp(system) {
        EventFilter.info(pattern = ".*42.*") intercept status()
        override def createCoffeeHouse() =
          actor(new Act { become { case CoffeeHouse.GetStatus => sender() ! CoffeeHouse.Status(42) } })
      }
    }
  }
}

