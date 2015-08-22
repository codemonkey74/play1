# Exercise 018: Changing Actor Behavior

In this exercise, we will demonstrate through the use of `become` and `stash` to modify actor

1. Run the `groom` command to initialize this exercise.
2. Reimplement the `Barista` actors behavior as a finite state machine:
    - Do not use the `busy` method.
    - Use `become`, `stash` and the `scheduler`.
3. Run the `test` command to verify the solution works as expected.
4. Run `CoffeeHouseApp` create some `Guest` actors and verify everything works as expected.
5. Run the `next` command to advance to the next exercise.
