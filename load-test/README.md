# K6 tests for GPS project

  - [01. Spontaneous payment creation](#01-spontaneous-payment-creation)
  - [02. Enrollment workflow](#02-enrollment-workflow)

This is a set of [k6](https://k6.io) tests related to the GPS (_Gestione Pagamenti Spontanei_) initiative.

To invoke k6 test passing parameter use -e (or --env) flag:

```
-e MY_VARIABLE=MY_VALUE
```

## 01. Spontaneous payment creation

Call to test the creation of a spontaneous payment:

```
k6 run --env VARS=local.environment.json --env TEST_TYPE=./test-types/load.json create_spontaneous_payment.js
```

where the mean of the environment variables is:

```json
  "environment": [
    {
      "env": "local",
      "host": "http://localhost:8080",
      "basePath": "organizations",
      "gpdBaseUrl": "https://api.dev.platform.pagopa.it/gpd/api/v1",
      "deleteDebtPosition": true
    }
  ]
```

`host`: it is the protocol and host of the spontaneous payment (GPS) service.

`basePath`: GPS's basepath service

`gpdBaseUrl`: it is the protocol and host of the GPD service. Used if the flag DELETE_DEBT_POSITION is set to true.

`deleteDebtPosition`: it is a flag that enables the delete of the debt position after it has been created by the spontaneous payment.


## 02. Enrollment workflow 

# TODO

Call to test the enrollment workflow:
1. create an organization without enrollments
2. create an enrollment to service for the organization
3. update an enrollment to service for the organization
4. update the organization
5. create another enrollment to service for the organization
6. get the single enrollment to service for the organization
7. get all enrollments for the organization
8. delete the first enrollment to service for the organization
9. delete the second enrollment to service for the organization
10. delete the organization

```
k6 run [TODO]
```