@CucumberTests
Feature: Trainer workload summary retrieval
  As an API client
  I want to retrieve a trainer's workload summary
  So that I can display their monthly and yearly workload

  Scenario: Retrieve existing trainer workload
    Given trainer workload exists for username "jane.smith"
    When client requests trainer workload for username "jane.smith"
    Then the response status should be 200
    And the response should contain trainerUsername "jane.smith" and firstName "John" and lastName "Doe" and status true

  Scenario: Trainer workload not found
    Given trainer workload does not exist for username "ghost.user"
    When client requests trainer workload for username "ghost.user"
    Then the response status should be 404
    And the error message should be "Error occurred during database interaction."
