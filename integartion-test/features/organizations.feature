Feature: All about Organizations

  Scenario: An organization creates an enrollment
    Given the organization "777777"
    When the organization enrolls in the service "service-1"
    Then the organization is enrolled in the service

  Scenario: An organization tries to create an enrollment, but service not found
    Given the organization "777777"
    When the organization enrolls in the service "service-not-found"
    Then the organization gets the status code 404

  Scenario: An organization deletes an enrollment
    Given the organization "777777" with the service "service-1"
    When the organization deletes the service "service-1"
    Then the organization isn't enrolled in the service
