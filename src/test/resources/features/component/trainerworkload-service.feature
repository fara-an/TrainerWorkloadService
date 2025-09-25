@CucumberTests
Feature: Trainer workload processing

  Scenario: Add a workload
    Given a workload request for trainer "john123" with action "ADD"
    When I process the workload request
    Then the trainer workload should be saved

  Scenario: Delete workload
    Given a workload request for trainer "john123" with action "DELETE"
    When I process the workload request
    Then the trainer workload should be saved

  Scenario: Invalid action should fail
    Given a workload request for trainer "john123" with action "INVALID"
    When I process the workload request
    Then an exception should be thrown and nothing saved

