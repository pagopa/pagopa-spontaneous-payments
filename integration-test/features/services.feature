Feature: All about Services

  Background:
    Given GPS running
    And creates the default service with id "service-1"
  
  Scenario: create a new service
    Given the body of the request for a service with id "service-2"
    When the client requests the creation of a service
    Then the client receives status code of 201
    
  Scenario: create a new service with a bad body request
    Given the bad body of the request for a service with id "service-3"
    When the client requests the creation of a service
    Then the client receives status code of 400
    
  Scenario: update a service
    Given the updated body of the request for a service with id "service-1"
    When the client requests a service update
    Then the client receives status code of 200
    
  Scenario: update a service with bad service id
    Given the updated body of the request for a service with id "service-non-found"
    When the client requests a service update
    Then the client receives status code of 404
    
  Scenario: delete a service
    Given the service to delete with id "service-2"
    When the client requests a service delete
    Then the client receives status code of 200
    
  Scenario: success services list
    When the client get all services
    Then the client receives status code of 200
    And the client retrieves the list of services

  Scenario: get service with success
    When the client get service "service-1"
    Then the client receives status code of 200

  Scenario: service not found
    When the client get service "service-not-found"
    Then the client receives status code of 404
  
    
  
