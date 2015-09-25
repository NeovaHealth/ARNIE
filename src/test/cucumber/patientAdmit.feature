Feature: Admit a patient to a ward

  Scenario: Admit patient Gandalf Grey to ward 6BN
    Given Patient "Gandalf" is admitted to ward "6BN"
    When an "A01" message is sent to ARNIE
    Then we receive an ACK with "AA"