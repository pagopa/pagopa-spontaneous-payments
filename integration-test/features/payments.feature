Feature: Create a spontaneous payment
  Background:
    Given GPS running
    And creates the service with id "service-for-payments"

  # Case OK
  Scenario: An organization creates a spontaneous payment
    Given the organization "777777" with an enrollment to service "service-for-payments"
    When the organization creates a spontaneous payment
    Then the organization gets the created status code 201
    And the organization gets the created Payment Position
    
  # Case KO: 404 non-existent service
  Scenario: An organization tries to create a spontaneous payment on a service that doesn't exist
    Given the organization creates the creditor institution "777777"
    When the organization enrolls the creditor institution on the service "service-not-found"
    Then the organization gets the created status code 404

  # Case KO: 404 non-existent creditor institution
  Scenario: An organization tries to create a spontaneous payment on a creditor institution that doesn't exist
    Given the organization tries to enroll the "service-1" on a not registered creditor institution "org-not-found"
    When the organization creates a spontaneous payment
    Then the organization gets the created status code 404
  
  # Case KO: 400 bad request - non-existent property in service configuration
  Scenario: An organization tries to create a spontaneous payment with a not configured property request
    Given the organization "777777" with an enrollment to service "service-1"
    When the organization tries to create a spontaneous payment with a property "property-not-found" not configured to service
    Then the organization gets the created status code 400
