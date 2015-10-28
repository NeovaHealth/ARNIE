package steps

import ca.uhn.hl7v2.model.Message
import support.*

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before
import static cucumber.api.groovy.Hooks.World

class testEnv01 {
    Message answer01

    def patient01 = new Patient()

    def router01 = new CamelConnector()
}

World() {
    new testEnv01()
}

Before() {
    creator = new MessageCreation()
}

Given(~/Patient "([^"]+)", born on "([^"]+)" with NHS number "([^"]+)" is admitted to ward "([^"]+)"./) {
    String patientName, String dob, String nhs_number, String ward ->

    patient01.with {
        familyName = patientName
        dateOfBirth = dob
        nhsNumber = nhs_number
        admitLocation = ward
    }

    assert patient01.familyName == patientName
    assert !patient01.dateOfBirth.contains(' ')
    assert patient01.nhsNumber.isNumber() && patient01.nhsNumber.length() == 10
    assert patient01.admitLocation == ward
    assert patient01.admitLocation.length() < 6
}

When(~/an "([^"]+)" message using HL7 version "([^"]+)" is sent to ARNIE with the name in the following field: "([^"]+)"/){ eventType, HL7version, nameField ->
    //TODO add closure to iterate over A01 - A40 //assert ('A'+('01'..'40')).contains(eventType)
    assert ('2.2'..'2.6').contains(HL7version)

    def msg = creator.createGenericMessage(eventType, patient01, HL7version)
    assert msg.PID[2].value == patient01.nhsNumber
    assert msg.PID[5][1].value == patient01.familyName
    assert msg.PID[7].value == patient01.dateOfBirth

    answer01 = router01.injectADTMessage(msg)
}

Then(~/an Acknowledgement message with "([^"]+)" is received./){ String ACK ->
    //assert answer01.getClass() == Message
    //.getParent()?
    //assert answer01.getMessageType().toString() == 'ACK'
    assert answer01.MSA[1].value == ACK
}



