# Exercise 017: Configure the Dispatcher

In this exercise, we will optimize parallelism through configuring a dispatcher.

1. Run the `groom` command to initialize this exercise.
2. Configure the `default-dispatcher`.
3. Use the default `fork-join-executor`.
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` with different values for `parallelism-max` as follows:
    - Less than the number of cores available.
    - Equal to the number of cores available.
    - More than the number of cores available.
    - Watch the throughput for each scenario.
6. Run the `next` command to advance to the next exercise.
