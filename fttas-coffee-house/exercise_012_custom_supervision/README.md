# Exercise 012: Custom Supervision

In this exercise, we will further explore resilience by implementing custom supervision.

1. Run the `groom` command to initialize this exercise.
2. Look up the default supervisor strategy in the `Akka` documentation.
3. Change `CoffeeHouse` as follows:
    - Caffeinated `Guest` actors should not restart.
    - Apply a custom supervision strategy that stops them instead.
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` and verify:
    - Create a `Guest` with an individual `caffeineLimit` less than the global one and watch its lifecycle.
    - Verify that the caffeinated `Guest` does not restart.
6. Run the `next` command to advance to the next exercise.
