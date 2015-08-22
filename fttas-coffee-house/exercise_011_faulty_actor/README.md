# Exercise 011: Create a Faulty Actor

In this exercise, we will explore resilience by managing a faulty actor.

1. Run the `groom` command to initialize this exercise.
2. Change `Guest` as follows:
    - Add a `caffeineLimit` parameter.
    - Add the `CaffeineException` case object extending `IllegalStateException` to the companion object.
    - Upon receiving `CoffeeFinished` throw the `CaffeineException` if `coffeeCount` exceeds `caffeineLimit`.
3. Change `CoffeeHouse` as follows:
    - So that a `Guest` can be created with a `caffeineLimit`.
    - Log the `Guest` path name instead of just the `Guest`.
4. Change `CoffeeHouseApp` as follows:
    - So that a `Guest` can be created with a `caffeineLimit`.
5. Run the `test` command to verify the solution works as expected.
6. Run `CoffeeHouseApp` and verify:
    - Create a `Guest` with an individual `caffeineLimit` less than the global one and watch its lifecycle.
    - *HINT*: Enter g 2 or guest 2 to create a `Guest` with a `caffeineLimit` of 2; if you omit the limit, `Int.MaxValue` will be used by default.
7. Run the `next` command to advance to the next exercise.
