# Exercise 013: Another Faulty Actor

In this exercise, we will introduce another faulty actor in the form of our `Barista` where sometimes they make the wrong coffee. When this happens, the `Guest` will complain and reorder. If the `Waiter` receives too many complaints, he will become frustrated.

1. Run the `groom` command to initialize this exercise.
2. Change the `Barista` as follows:
    - Add an `accuracy` parameter of type `Int` expressing a percentage.
    - Get a random `Int` value less than 100.
    - If the random `Int` is less than `accuracy`, prepare the correct `Coffee` otherwise prepare a wrong one.
3. Change the `Waiter` as follows:
    - Add a `Complaint` message with parameter `coffee` of type `Coffee`.
    - Add a `FrustratedException` case object extending `IllegalStateException` to the companion object.
    - Add a `barista` parameter of type `ActorRef` and a `maxComplaintCount` parameter of type `Int`.
    - Keep track of the number of `Complaint` messages received.
    - If more `Complaint` messages arrive than the `maxComplaintCount`, throw a `FrustratedException`, else send `PrepareCoffee` to the `Barista`.
4. Change the `Guest` as follows:
    - On receiving the wrong `Coffee`, send a `Complaint` to the `Waiter`.
    - Which argument needs to be given for `coffee`?
5. Change `CoffeeHouse` as follows:
    - For `accuracy` use a configuration value with key `coffee-house.barista.accuracy`.
    - For `maxComplaintCount` use a configuration value with key `coffee-house.waiter.max-complaint-count`.
    - Don't forget to use the new parameters when creating the `Barista` and `Waiter`.
6. Run the `test` command to verify the solution works as expected.
7. Run `CoffeeHouseApp` and verify:
    - Create a `Guest` and see what happens when the `Waiter` gets frustrated.
    - *HINT*: You might need to use small `accuracy` and `maxComplainCount` values.
8. Run the `next` command to advance to the next exercise.
