# Exercise 008: Keeping Actors Busy

In this exercise, we will introduce a `Barista` actor who specializes in making our fine caffeinated beverages and will keep our other actors busy.

1. Run the `groom` command to initialize this exercise.
2. Create the `Barista` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Add a `prepareCoffeeDuration` parameter of type `FiniteDuration`.
    - Define a `Props` factory.
    - Create a `PrepareCoffee` message with parameters of `coffee` type `Coffee` and `guest` type `ActorRef`.
    - Create a `CoffeePrepared` message with parameters of `coffee` type `Coffee` and `guest` type `ActorRef`.
    - Define the behavior as when `PrepareCoffee(coffee, guest)` is received:
        - Busily prepare coffee for `prepareCoffeeDuration`.
        - Respond with `CoffeePrepared(coffee, guest)` to the sender.
        - *HINT*: Use `busy` from the `coffeehouse` package object in the `common` project to simulate being busy while preparing the `Coffee`.
3. Change `Waiter` as follows:
    - Add a reference to the `Barista` actor.
    - Instead of serving coffee immediately, defer to the `Barista` for preparation.
4. Change `CoffeeHouse` as follows:
    - Create a `Barista` actor with name *"barista"*.
    - Use a `createBarista` factory method.
    - For `prepareCoffeeDuration`, use a configuration value with key `coffee-house.barista.prepare-coffee-duration`.
5. Run the `test` command to verify the solution works as expected.
6. Run `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged once.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - Make sure the `Guest` actors are enjoying their coffee.
7. Run the `next` command to advance to the next exercise.
