Feature: Create a spontaneous payment
  Background:
    Given GPS running

  Scenario: An organization creates a spontaneous payment
    Given the organization "777777" with an enrollment to service "service-1"
    When the organization creates a spontaneous payment
    Then the organization gets the created status code 201 
    And the organization gets the created Payment Position
