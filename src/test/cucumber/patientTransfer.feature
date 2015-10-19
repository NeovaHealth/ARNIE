Feature: Transfer an admitted patient to another ward

  Scenario: Transfer patient Arthur Nudge to ward 6B South
    Given Patient "Arthur Nudge", who has already been admitted, has to be transferred to another ward "06BS".
    When an "A02" message using HL7 version "2.4" is sent to ARNIE with the target location in "PID-1"
    Then an Acknowledgement message with "AA" is received.