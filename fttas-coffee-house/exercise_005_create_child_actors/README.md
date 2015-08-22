# Exercise 005: Create Child Actor

In this exercise, we will use `CoffeeHouse` to create `Guest` as a child actor.

1. Run the `groom` command to initialize this exercise.
2. Create the `Guest` actor in the `com.typesafe.training.coffeehouse` package as follows:
    - Create a `Props` factory for `Guest`.
    - Implement the behavior as *empty*.
    - *HINT*: See `Actor.emptyBehavior`.
3. Change `CoffeeHouse` as follows:
    - Add `CreateGuest` message.
    - Use a `createGuest` factory method that creates a `Guest` without a name.
    - Upon receiving the `CreateGuest` message call the factory method.
4. Change `CoffeeHouseApp` as follows:
    - Remove the anonymous actor.
    - When handling `Command.Guest`, send `CreateGuest` to `CoffeeHouse`.
    - Make sure you account for the creating of 1 or more `Guest` actors.
5. Turn on `lifecycle` debugging.
6. Run the `test` command to verify the solution works as expected.
7. Run `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged once.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - *HINT*: Enter `5 g` or `5 guest` to create five `Guest` actors. If you omit the count, `1` is used by default.
8. Run the `next` command to advance to the next exercise.
