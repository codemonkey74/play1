# Exercise 006: Actor State

In this exercise, we will implement state by tracking a `Guest` actor's favorite `Coffee`.

1. Run the `groom` command to initialize this exercise.
2. Create the `Waiter` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Define a `Props` factory.
    - Create a `ServeCoffee` message with parameter `coffee` of type `Coffee`.
    - Create a `CoffeeServed` message with parameter `coffee` of type `Coffee`.
    - Define the behavior as when `ServeCoffee(coffee)` is received, respond with `CoffeeServed(coffee)` to the sender.
3. Change `Guest` as follows:
    - Create a `CoffeeFinished` message.
    - Add a `waiter` parameter of type `ActorRef`.
    - Add a `favoriteCoffee` parameter of type `Coffee`.
    - Add a local mutable `coffeeCount` field of type `Int`.
    - Define the behavior as:
        - When `CoffeeServed(coffee)` is received:
            - Increase the `coffeeCount` by one.
            - Log *"Enjoying my {coffeeCount}.yummy{coffee}!"* at `info`.
        - When `CoffeeFinished` is received, respond with `ServeCoffee(favoriteCoffee)` to `waiter`.
4. Change `CoffeeHouse` as follows:
    - Use a `createWaiter` factory method that creates the `Waiter` with name *"waiter"*.
    - Add `favoriteCoffee` parameter of type `Coffee` to the `CreateGuest` message.
    - Update the `createGuest` factory method to account for the `waiter` and `favoriteCoffee` parameters.
5. Change `CoffeeHouseApp` to account for the `favoriteCoffee` parameter required by the `CreateGuest` message.
6. Run the `test` command to verify the solution works as expected.
7. Run `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged once.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - *HINT*: Enter g {coffee} or guest {coffee} where {coffee} has to be the first letter of one of the defined coffees (`a`, `m`, or `c`). If you omit {coffee}, `Akkaccino` will be used by default.
8. QUIZ: Why don't you see any log messages from `Guest` actors enjoying their coffee?
9. Run the `next` command to advance to the next exercise.
