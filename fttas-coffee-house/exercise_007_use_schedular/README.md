# Exercise 007: Use Scheduler

In this exercise, we will implement the Akka `Scheduler` to simulate the `Guest` ordering more `Coffee`.

1. Run the `groom` command to initialize this exercise.
2. Change `Guest` as follows:
    - Add a `finishCoffeeDuration` parameter of type `scala.concurrent.duration.FiniteDuration`.
    - Change the behavior on receiving `CoffeeServed` to schedule the sending of `CoffeeFinished` to the `Guest`.
    - QUIZ: What other changes are needed for the guest to enjoy their coffee?
3. Change `CoffeeHouse` as follows:
    - Adjust the code for creating a new `Guest`.
    - For `finishCoffeeDuration`, use a configuration value with key `coffee-house.guest.finish-coffee-duration`.
    - To get the configuration value, use the `getDuration` method on `context.system.settings.config`.
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged once.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - Make sure the `Guest` actors are enjoying their coffee.
    - *HINT*: Enter g {coffee} or guest {coffee} where {coffee} has to be the first letter of one of the defined coffees (`a`, `m`, or `c`). If you omit {coffee}, `Akkaccino` will be used by default.
6. Run the `next` command to advance to the next exercise.
