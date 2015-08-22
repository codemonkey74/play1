# Exercise 002: Create a Top Level Actor

In this exercise, we will make `CoffeeHouse` a top-level actor and implement some configuration properties.

1. Run the `groom` command to initialize this exercise.
2. In the `CoffeeHouse` actor:
    - Implement a `Props` factory.
    - Log "CoffeeHouse Open" at `debug`.
3. In `CoffeeHouseApp` create a top-level actor named *"coffee-house"*.
4. Create a configuration file that does the following:
    - Set the Akka logging level to `debug`.
    - Turn on logging of `unhandled` messages.
    - Use the `Slf4jLogger`.
5. Run the `test` command to verify the solution works as expected.
6. Run the `CoffeeHouseApp` and verify *"CoffeeHouse Open"* is logged once.
7. Run the `next` command to advance to the next exercise.
