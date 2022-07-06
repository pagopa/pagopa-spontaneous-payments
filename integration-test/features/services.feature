Feature: All about Services

  Background:
    Given GPS running

  Scenario: success services list
    Given some services in the DB
    When the client get all services
    Then the client receives status code of 200
    And the client retrieves the list of services

  Scenario: get service with success
    Given some services in the DB
    When the client get service "service-1"
    Then the client receives status code of 200

  Scenario: service not found
    When the client get service "service-not-found"
    Then the client receives status code of 404
