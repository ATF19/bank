
# Bank Backend

### Running the project
#### Using Docker
1. Simply run `docker run -it --rm -p 9001:9001 --name bank-backend atf19/bank-backend`

#### Using Docker Compose
1. Navigate to the `Backend` folder using `cd Backend`
2. Run `docker-compose pull` or `docker compose pull` to pull the image
3. Run `docker-compose up` or `docker compose up` to start it


#### Using JAVA (11+)
1. Navigate to the `Backend/target` folder using `cd Backend/target`
2. Run the application with `java -jar backend-1.0.0-SNAPSHOT.jar`

### Testing the API
After starting the backend, you can use Swagger UI to test it by simply opening http://localhost:9001/swagger-ui/index.html


### Concepts
While writing this small backend, the following concepts were used:
* **DDD**: Domain Driven Design is used to focus on the domain requirements of the application.
* **Onion Architecture**: The code is structured using the onion architecture with 4 big layers (domain, application, infrastructure and presentation).
* **Event Sourcing**: I chose event sourcing to make it easier to implement (in the future) accounts and operations histories which is always needed in a banking app.
* **TDD**: TDD is used to drive the implementation.

### Technologies
* Kotlin
* Spring Boot
* H2 as database
* JUnit 5
* Mockk
* AssertJ
* Flyway
* Swagger

### Model
There are two main contextes in the application, an `Account` context and a `Transfer` context.
These two contextes are separated from each other and can be extracted into different services.

After doing a transfer, it is needed to deduct an amount from the sender (an account) and give it to the receiver (also an account). To achieve this without coupling the `Account` and the `Transfer` together, an event is used and once a transfer is created, a listener will update the accounts accordingly.
Currently, a `Guava EventBus` is used but in real-life it would be better to rely on a queue.

Concurrency is handled by using optimistic locking with a version number on the event and it should always different that what's already there (a constraint in the DB).


Thank you.

*Atef Najar*
