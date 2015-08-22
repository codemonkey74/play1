# Exercise 019: Use the Ask Pattern

This exercise will demonstrate how to interact with an actor from the outside by way of the `ask` pattern.

1. Run the `groom` command to initialize this exercise.
2. Change `CoffeeHouse` as follows:
    - Add the `GetStatus` case object.
    - Add the `Status` case class that extends `Msg` and has a `guestCount` parameter of type `Int`.
    - On receiving `GetStatus` respond with `Status` initialized with the current number of `Guest` actors.
3. Change `CoffeeHouseApp` as follows:
    - Handle a `StatusCommand` by asking `CoffeeHouse` to get the status.
    - Register callbacks logging the number of `Guest` actors at `info` and any failure at `error`.
    - For the `ask` timeout, use a configuration value with key `coffee-house.status-timeout`.
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` create some `Guest` actors and verify everything works as expected.
6. Run the `next` command to advance to the next exercise.
