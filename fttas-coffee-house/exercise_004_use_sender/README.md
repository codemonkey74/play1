# Exercise 004: Use the Sender

In this exercise, we will use the `implicit` sender to respond from `CoffeeHouse`.

1. Run the `groom` command to initialize this exercise.
2. Change `CoffeeHouse` as follows:
    - Instead of logging *"Coffee Brewing"*, respond to the sender with a *"Coffee Brewing"* message.
3. In `CoffeeHouseApp` create an anonymous actor that does the following:
    - In the constructor send `CoffeeHouse` a *"Brew Coffee"* message.
    - Log the message received at `info`.
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` and verify:
    - *"CoffeeHouse Open"* is logged once.
    - *"Coffee Brewing"* is logged once.
6. Run the `next` command to advance to the next exercise.
