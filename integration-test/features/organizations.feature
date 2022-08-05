Feature: All about Organizations
  Background:
    Given GPS running

  Scenario: An organization creates an enrollment
    Given the organization "777777"
    When the organization enrolls in the service "service-1"
    Then the organization gets the status code 201
    And the service is listed in the organization's details

  Scenario: An organization tries to create an enrollment, but service not found
    Given the organization "777777"
    When the organization enrolls in the service "service-not-found"
    Then the organization gets the status code 404

  Scenario: An organization deletes an enrollment
    Given the organization "777777" with the service "service-1"
    When the organization deletes the service "service-1"
    Then the organization gets the status code 200
    And the service is not found for the organization

  Scenario: An organization change the IBAN
    Given the organization "777777" with the service "service-1"
    When the organization changes the service IBAN with "ABCD"
    Then the organization gets the status code 200
    And the service for the organization has the IBAN "ABCD"

  Scenario: An organization change the IBAN
    Given the organization "777777" with the service "service-1"
    When the organization changes the service IBAN with "ABCD"
    Then the organization gets the status code 200
    And the service for the organization has the IBAN "ABCD"

  Scenario: An organization is disabled
    Given the organization "777777"
    When the organization set the status to "DISABLED"
    Then the organization gets the status code 200
    And the status is "DISABLED" in the organization's details

  Scenario: An organization is disabled
    Given the organization "777777"
    When the organization set the status to "DISABLED"
    Then the organization gets the status code 200
    And the status is "DISABLED" in the organization's details
    
  Scenario: An organization try to create an enrollment to a service without some mandatory parameters
    Given the organization "777777" with the service "service-1" without mandatory parameters
    Then the organization gets the status code 400
