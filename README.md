# ABC Customer Onboarding

The RESTful APIs allow customer to register, login, check the user account overview 

## Tech stack 

- **Spring Boot**

  The ABC Customer onboarding API is built by Springboot. RESTful API can provide uniform interface and objects in REST are always manipulated from the URI, which is easy for different endpoints to consume.

- **H2**

  H2 database is used as the Database. Four Tables were created:

  | Table           | Primary Key | Foreign Key           | Description                          |
  |-----------------|-------------|-----------------------|--------------------------------------|
  | Customer        | id          | customerId, accountId | Store the basic customer information |
  | CustomerDetails | customerId  | addressId             | Store the customer details           |
  | Account         | accountId   | None                  | Store the account information        |
  | Address         | addressId    | None                  | Store the customer address detaisls  |

- **Docker**

  The service is deployed using Docker. Containerization provides a loosely-coupled way to deploy the application. At the same time, auto-scaling feature and resilliency can be provided by several container orchastration system, such as Kubernetes, Docker Swarm. Therefore, if there's more and more requests coming to the service, resiience and concurrency can be guaranteed.

- **Swagger UI**

  Swagger UI is a tool that allows you to visualize and interact with your RESTful APIs. API descriptions, example requests/responses can also be found in the Swagger UI
- **Spring Security**
  
  The use authentication is implemented by Spring Security. A JWT token will be generated if the input credentials are correct. 
## Quickstart

### With Docker running locally

1. Make sure Docker is running on your machine. 

2. Clone the codebase to your local machine.

   ```bash
   git clone https://github.com/yuchen9459/Starbux.git
   ```

3. Switch to the correct folder

   ```bash
   cd onboarding
   ```

4. Start the application with following command

   ```bash
   docker-compose up --build -d
   ```
5. Access to the Swagger UI via following URL:

   ```bash
   http://localhost:8080/swagger-ui/index.html
   ```
5. The exported Postman Collection can be found under the name. Import to the Postman would be enough to get started with the API testing.

   ```bash
   ABC.postman_collection.json
   ```
   
### Without Docker running locally

1. Clone the codebase to your local machine.

   ```bash
   git clone https://github.com/yuchen9459/Starbux.git
   ```
2. Run the application by Intellij, please make sure the port 8080 is available.

## Highlights
1. `@Cacheable` annotation was used to make sure the load balancing with the database. In the real world scenario, we can replace it with the Redis cache, which can be configured in a finer level.
2. The IBAN conversion is done by the third part library called [iban4j](https://github.com/arturmkrtchyan/iban4j/tree/master)
3. Necessary unit tests and integration tests have been added to make sure the code is easy to be maintained.
4. The allowed country list is configured in the properties file, which make it easier to be updated by simply redeploying the application without any codebase changes.
5. Detailed API documentation can be found via the Swagger UI.
6. Global exception handlers was added to make sure the proper error code and http status will be returned.


## Thoughts 
This is only a simple application to realize the simple signup, login and get overview functionalities. To make it more completed, the following features should be added in the next stage:
1. The deposit/ withdraw API endpoints to change the balance, which should also be transactional.
2. The default account which is created during the user registration is Saving account, an endpoint to create Current account can be added.
3. Role based authorization can be added to make sure the fine-grained level of access to the data.
4. The Redis cache database can be introduced to ensure the quick response.
5. API versioning.

