# Spontaneous Payments

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pagopa_pagopa-api-config&metric=alert_status)](https://sonarcloud.io/project/overview?id=pagopa_pagopa-spontaneous-payments)

Spring application exposes API to manage the _Spontaneous Payments_.

---
## Api Documentation 📖
See the [Swagger 2 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-spontaneous-payments/main/openapi/swagger.json)

See the [OpenApi 3 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-spontaneous-payments/main/openapi/openapi.json)

---

## Technology Stack
- Java 11
- Spring Boot
- Spring Web
- Azure CosmosDB
---

## Start Project Locally 🚀

### Prerequisites
- docker
- account on dockerhub

> 👀 The docker account is needed to be able to pull the image oracle-db-ee and for which accept the Terms of Service via web. [Open this link](https://hub.docker.com/_/oracle-database-enterprise-edition) and click on "Proceed to Checkout" button.

Remember to login to the local docker with `docker login` command

### Run
Todo

## Develop Locally 💻

### Prerequisites
- git
- maven
- jdk-11
- docker
- cosmosdb emulator

### Configure Cosmos emulator 
Launch the script in `utilities` folder:

`
sh cosmos.sh <port> <java_home>
`

### Testing 🧪

#### Unit testing
Todo

#### Integration testing
Todo

#### Load testing
Todo

## Contributors 👥
Made with ❤️ from PagoPa S.p.A.

### Mainteiners
See `CODEOWNERS` file
