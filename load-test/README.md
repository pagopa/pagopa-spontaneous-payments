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

On Azure

```
...
```

On localhost 

```
k6 run --env VARS=local.environment.json --env TEST_TYPE=./test-types/load.json create_spontaneous_payment.js
```

where the mean of the environment variables is:

GPS_BASE_URL: it is the protocol and host of the spontaneous payment (GPS) service.

ORGANIZATION_FISCAL_CODE: it is the organization on which the tests will be launched (the organization must have been previously registered on the database and must be associated with the appropriate service).

DELETE_DEBT_POSITION: it is a flag that enables the delete of the debt position after it has been created by the spontaneous payment.

GPD_BASE_URL: it is the protocol and host of the GPD service. Used if the flag DELETE_DEBT_POSITION is set to true.



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