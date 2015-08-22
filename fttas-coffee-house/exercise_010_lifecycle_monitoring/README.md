# Exercise 010: Lifecycle Monitoring

Sometimes we need to perform certain tasks before stopping an actor. In this exercise, we will explore this idea by watching for the `Termination` message.

1. Run the `groom` command to initialize this exercise.
2. Change `CoffeeHouse` to monitor each `Guest` as follows:
    - When the `Guest` terminates, remove the `Guest` from caffeineLimit bookkeeping.
    - Log *"Thanks {guest}, for being our guest!"* at `info`.
3. Run the `test` command to verify the solution works as expected.
4. Run `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged once.
    - Lifecycle debug messages are logged.
    - Make sure the correct number of `Guest` creations were logged.
    - Make sure the `Guest` actors are enjoying their coffee.
    - Make sure your `Guest` actors are stopped as expected.
5. Run the `next` command to advance to the next exercise.
