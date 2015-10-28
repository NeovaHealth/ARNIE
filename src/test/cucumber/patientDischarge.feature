Feature: Discharge an admitted patient

  Scenario: Discharge patient Arthur Nudge
    Given Patient "Arthur Nudge", who has already been admitted, should be discharged.
    When an A03 message using HL7 version "2.4" is sent to ARNIE.
    Then an Acknowledgement message with "AA" is received.