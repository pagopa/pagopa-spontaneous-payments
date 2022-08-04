Feature: Create a spontaneous payment
  Background:
    Given GPS running

  # Case OK
  Scenario: An organization creates a spontaneous payment
    Given the organization "777777" with an enrollment to service "service-1"
    When the organization creates a spontaneous payment
    Then the organization gets the created status code 201 
    And the organization gets the created Payment Position

  # Case KO: 404 non-existent service
	  Scenario: An organization tries to create a spontaneous payment on a service that doesn't exist
	  Given the organization "777777" with an enrollment to service "service-not-found"
	  Then the organization gets the created status code 404

  # Case KO: 400 bad request - non-existent property in service configuration
	  Scenario: An organization tries to create a spontaneous payment with a not configured property request
	  Given the organization "777777" with an enrollment to service "service-1"
	  When the organization creates a spontaneous payment with a not configured property in the request
	  Then the organization gets the created status code 400
