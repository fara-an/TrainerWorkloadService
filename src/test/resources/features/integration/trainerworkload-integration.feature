@CucumberIT
Feature: Trainer workload integration with JMS

  Scenario: Process ADD workload message
    Given a trainer workload ADD message for trainer "john"
    When the message is sent to the workload queue
    Then the workload for "john" should exist in the database

  Scenario: Process DELETE workload message
    Given a trainer workload ADD message for trainer "jane"
    And the message is sent to the workload queue
    When a trainer workload DELETE message for trainer "jane" is sent
    Then the workload for "jane" should be removed from the database

  Scenario: Reject invalid action type
    Given an invalid trainer workload message with action "UPDATE" for trainer "bob"
    When the message is sent to the workload queue
    Then the workload for "bob" should not exist in the database
