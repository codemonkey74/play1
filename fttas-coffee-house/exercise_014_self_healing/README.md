# Exercise 014: Implement Self Healing

In this exercise, we will correct a problem introduced in the last exercise by implement self-healing.

1. Run the `groom` command to initialize this exercise.
2. QUIZ: Why does the message flow in exercise 013 get interrupted?
3. Reinitiate the message flow by providing the `Waiter` actors supervisor with all the necessary information.
    - *HINT*: Think supervision strategy!
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` and verify that `Guest` actors are served even after the `Waiter` gets frustrated.
6. Run the `next` command to advance to the next exercise.
