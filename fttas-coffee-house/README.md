# Fast Track to Akka with Scala

## Coffee House

Welcome to the Coffee House. Here you will delight in one of the three caffeinated concoctions: `Akkaccino`, `MochaPlay` and `CaffeScala` and learn the basics of Akka with Scala along the way. To achieve this, you will work through the series of exercises organized by topic (listed below) as laid out in the Fast Track to Akka with Scala slide deck. Make sure you have the deck handy as you will want to refer to it for guidance.

## Prerequisites

This course is best suited for individuals that have knowledge of Scala as covered in our [Fast Track to Scala](http://www.typesafe.com/how/training/fasttracktoscala) course. You will also need access to the Internet and a computer with the following software installed:

- JVM 1.7 or higher
- [Scala 2.11](http://www.scala-lang.org/documentation/) or higher
- [sbt 13.7](http://www.scala-sbt.org/0.13/docs/index.html) or higher
- A Unix compatible shell

## How Students will Use Courseware

This project contains the base code needed for working through the exercises. During each exercise, you will add new code to solve the problem set, and when done will be a great reference for the basics of Akka. In each exercise, there is a central `CoffeeHouseApp.scala` file that you will modify and use during the course of the exercises that will show you how things are progressing. We also include completed tests that you so you can be sure your solution is correct.

### Sbt

This project does not provide a GUI so you will rely on a `sbt` session for feedback. If you are new to [sbt](http://www.scala-sbt.org/documentation.html), that is okay, but there is a couple of commands you want to familiarize yourself with.

To load and start a `sbt` session, make sure you are in the `fttas-coffee-house` directory and type `sbt` in your terminal window.

`$ sbt`

#### Clean

To clean your project execute the following command from your `sbt` session, this deletes all generated files in the `target` directory.

`[001] some_exercise > clean`

#### Compile

To compile your project execute the following command from your `sbt` session, this compiles the source in the `src/main/scala` directory.

`[001] some_exercise > compile`

#### Reload

To reload your project execute the following command from your `sbt` session, this reloads the build definition `build.sbt`, `project/*.scala` and `project/*.sbt` files. Reloading is a requirement if you change the build definition.

`[001] some_exercise > reload`

#### Test

To run tests with your project execute the following command from your `sbt` session, this compiles and runs all tests.

`[001] some_exercise > test`

#### Next

To move to the next exercise, you will use the following command.

`[001] some_exercise > next`

#### Prev

To go to the previous exercise, you will use the following command.

`[001] some_exercise > prev`

#### Groom

Before starting a new exercise, you will first need to initialize it with the following command. This will copy the source from the previous exercise.

`[001] some_exercise > groom`

#### Manual

To view the instructions for the current exercise, you will use the following command. This will display the directions in the terminal window.

`[001] some_exercise > man`

#### Eclipse (Only if your IDE is Eclipse based.)

To prepare the current project for the Eclipse IDE, you will need to run following command. You should do this *after* you have run `groom`.

`[001] some_exercise > eclipse`

After running this command, you can then use `File > Import` to run the `Import Wizard` for eclipse.

# Exercises

1. When you first run `sbt`, you should be in the `[run the man command to get started] base >` directory.
2. Run the `next` command, and this will forward you to the `[run the man command to get started] common >` project.
3. Enjoy! Have fun and welcome to the world of `Akka with Scala`.

