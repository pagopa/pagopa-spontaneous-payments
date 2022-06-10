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
## Technology Stack
- Java 11
- Spring Boot
- Spring Web
- Hibernate
- JPA
- [Spring cosmos api](https://docs.microsoft.com/it-it/azure/cosmos-db/sql/sql-api-spring-data-sdk-samples)

---

## Start Project Locally 🚀

### Prerequisites
- docker
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


### Prerequisites
- git
- maven
- jdk-11
- docker

### Run the project
The easiest way to develop locally is start only db container and run spring-boot application.
```
...
```


### Testing 🧪

#### Unit testing

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



