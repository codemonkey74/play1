# [Optional] Exercise 020: Use an Akka Extension for Configuration

In this exercise, we will create an Akka `extension` for configuration related settings.

1. Run the `groom` command to initialize this exercise.
2. Create an `extension` for all configuration settings related to `CoffeeHouse`:
    - Define the `Settings` singleton object and the `Settings` class.
    - Define the `SettingsActor` trait for convenient access inside our actors.
    - Provide `val` attributes for all settings underneath `coffee-house`.
3. Replace all occurrences of direct setting access:
    - Outside of actors, use `Settings(system).something`.
    - Inside actors mix in the `SettingsActor` trait.
4. Run the `test` command to verify the solution works as expected.
5. Run `CoffeeHouseApp` create some `Guest` actors and verify everything works as expected.
6. Run the `next` command to advance to the next exercise.
