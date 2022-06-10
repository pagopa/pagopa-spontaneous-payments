<<<<<<< HEAD
# spontaneous-payments
PagoPA service for citizen/corporate spontaneous payments
---
## Api Documentation 📖
See the [OpenApi 3 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-spontaneous-payments/main/openapi/openapi.yaml)

In local env typing following url on browser for ui interface:
```
http://localhost:8080/swagger-ui/index.html

```
or that for `yaml` version
```http://localhost:8080/v3/api-docs/```

---
=======
# Spontaneous Payments

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pagopa_pagopa-api-config&metric=alert_status)](https://sonarcloud.io/project/overview?id=pagopa_pagopa-spontaneous-payments)

Spring application exposes API to manage the _Spontaneous Payments_.

---
## Api Documentation 📖
See the [Swagger 2 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-spontaneous-payments/main/openapi/swagger.json)

See the [OpenApi 3 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-spontaneous-payments/main/openapi/openapi.json)

---

>>>>>>> 2cab4fafa4f1caf461a017855db5be7a13773420
## Technology Stack
- Java 11
- Spring Boot
- Spring Web
<<<<<<< HEAD
- Hibernate
- JPA
- [Spring cosmos api](https://docs.microsoft.com/it-it/azure/cosmos-db/sql/sql-api-spring-data-sdk-samples)

=======
- Azure CosmosDB
>>>>>>> 2cab4fafa4f1caf461a017855db5be7a13773420
---

## Start Project Locally 🚀

### Prerequisites
- docker
<<<<<<< HEAD
- [Cosmos emulator](https://docs.microsoft.com/it-it/azure/cosmos-db/linux-emulator?tabs=sql-api%2Cssl-netstd21)

### Run docker container

Under main project folder typing :
`docker-compose up --build`
>**NOTE** : before that compile `gpd` service with `mvn clean package` command

If all right, eventually you'll see something like that:
```sh
...
...
```

---

## Develop Locally 💻


=======
- account on dockerhub

> 👀 The docker account is needed to be able to pull the image oracle-db-ee and for which accept the Terms of Service via web. [Open this link](https://hub.docker.com/_/oracle-database-enterprise-edition) and click on "Proceed to Checkout" button.

Remember to login to the local docker with `docker login` command

### Run
Todo

## Develop Locally 💻

>>>>>>> 2cab4fafa4f1caf461a017855db5be7a13773420
### Prerequisites
- git
- maven
- jdk-11
- docker
<<<<<<< HEAD

### Run the project
The easiest way to develop locally is start only db container and run spring-boot application.
```
...
```

=======
- cosmosdb emulator

### Configure Cosmos emulator 
Launch the script in `utilities` folder:

`
sh cosmos.sh <port> <java_home>
`
>>>>>>> 2cab4fafa4f1caf461a017855db5be7a13773420

### Testing 🧪

#### Unit testing
<<<<<<< HEAD

Under `...` folder typing `mvn clean verify`, if all right you'll see following stuffs

```sh
[INFO] Results:
```

#### Integration testing

under `...` folder typing

```sh
 bash api-test/run_test.sh l int
```

if all  right you'll see something like that :

```sh
┌─────────────────────────┬───────────────────┬──────────────────┐
│                         │          executed │           failed │
├─────────────────────────┼───────────────────┼──────────────────┤
│              iterations │                 1 │                0 │
├─────────────────────────┼───────────────────┼──────────────────┤
│                requests │                 9 │                0 │
├─────────────────────────┼───────────────────┼──────────────────┤
│            test-scripts │                18 │                0 │
├─────────────────────────┼───────────────────┼──────────────────┤
│      prerequest-scripts │                10 │                0 │
├─────────────────────────┼───────────────────┼──────────────────┤
│              assertions │                13 │                0 │
├─────────────────────────┴───────────────────┴──────────────────┤
│ total run duration: 1003ms                                     │
├────────────────────────────────────────────────────────────────┤
│ total data received: 5.25kB (approx)                           │
├────────────────────────────────────────────────────────────────┤
│ average response time: 79ms [min: 8ms, max: 207ms, s.d.: 61ms] │
└────────────────────────────────────────────────────────────────┘
```


#### Load testing

under `...` folder typing

```sh
 bash api-test/run_test.sh l load
```

---

### Mainteiners
See `CODEOWNERS` file



=======
Todo

#### Integration testing
Todo

#### Load testing
Todo

## Contributors 👥
Made with ❤️ from PagoPa S.p.A.

### Mainteiners
See `CODEOWNERS` file
>>>>>>> 2cab4fafa4f1caf461a017855db5be7a13773420
