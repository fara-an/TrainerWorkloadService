Feature: Trainer workload synchronization between GymApp and TrainerWorkloadService


  Scenario: Add training updates trainer workload
    When I add a training for trainer "Jane.Smith" with spec "Cardio" and trainee "Emily.Brown" with duration 60 minutes
    And I wait until the workload service processes the message
    Then the trainer workload summary for "Jane.Smith" should return 200 code

  Scenario: Delete training updates trainer workload
    Given I add a training for trainer "John.Doe" with spec "Strength Training" and trainee "Michael.White" with duration 45 minutes
    And I wait until the workload service processes the message
    When I delete that training for trainer "John.Doe" with a trainee "Michael.White"
    And I wait until the workload service processes the message
    Then the trainer workload summary for "John.Doe" should return 200 code

  Scenario: Requesting workload for unknown trainer should return error
    When I request workload summary for "unknown.trainer"
    Then I should receive a 404 Not Found error
