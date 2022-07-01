Feature: All about Services

  Scenario: success services list
    Given some services in the DB
    When the client get all services
    Then the client receives status code of 200
    And the client retrieves the list of services

  Scenario: get service with success
    Given some services in the DB
    When the client get service "d"
    Then the client receives status code of 200

  Scenario: service not found
    Given no services in the DB
    When the client get service 1
    Then the client receives status code of 404
