# Spontaneous Payments
PagoPA service for citizen/corporate spontaneous payments
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pagopa_pagopa-api-config&metric=alert_status)](https://sonarcloud.io/project/overview?id=pagopa_pagopa-spontaneous-payments)
---
## Api Documentation 📖
Spring application exposes API to manage the _Spontaneous Payments_.

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

### Run
Todo

### Prerequisites
- git
- maven
- jdk-11
- docker
- cosmosdb emulator

### Run the project
The easiest way to develop locally is start only db container and run spring-boot application.
```
...
```

### Configure Cosmos emulator 
Launch the script in `utilities` folder:

`
sh cosmos.sh <port> <java_home>
`
### Testing 🧪

#### Unit testing

Typing `mvn clean verify`, if all right you'll see following stuffs

```sh

```

#### Integration testing
Todo

#### Load testing
Todo

## Contributors 👥
Made with ❤️ from PagoPa S.p.A.

### Mainteiners
See `CODEOWNERS` file
