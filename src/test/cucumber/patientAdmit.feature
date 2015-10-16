Feature: Admit a patient to a ward

  Scenario: Admit patient Arthur Nudge to ward 6B North
    Given Patient "Arthur Nudge", born on "07/06/1980" with NHS number "0123456789" is admitted to ward "06BN".
    When an "A01" message using HL7 version "2.4" is sent to ARNIE with the name in the following field: "PID-5-1"
    Then an Acknowledgement message with "AA" is received.