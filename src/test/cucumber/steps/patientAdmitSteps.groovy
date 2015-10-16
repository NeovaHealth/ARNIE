package steps

import ca.uhn.hl7v2.model.Message
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith
import support.MessageCreation
import support.Patient
import support.Router

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before
import static cucumber.api.groovy.Hooks.World

/**
 * Created by gregorlenz on 24/09/15.
*/
@RunWith(Cucumber.class)
class testEnvironment {
    Message answer

    def patient = new Patient()
    def router = new Router()
}

World() {
    new testEnvironment()
}

Before() {
    creator = new MessageCreation()
}

Given(~/Patient "([^"]+)", born on "([^"]+)" with NHS number "([^"]+)" is admitted to ward "([^"]+)"./) { String patientName, String dob, String nhs_number, String ward ->
    patient.with {
        familyName = patientName
        dateOfBirth = dob
        nhsNumber = nhs_number
        admitLocation = ward
    }

    assert patient.familyName == patientName
    assert !patient.dateOfBirth.contains(' ')
    assert patient.nhsNumber.isNumber() && patient.nhsNumber.length() == 10
    assert patient.admitLocation == ward
    assert patient.admitLocation.length() < 5
}

When(~/an "([^"]+)" message using HL7 Version "([^"]+)" is sent to ARNIE with the name in the following field: "([^"]+)"/){ eventType, HL7version, nameField ->
    //TODO add closure to iterate over A01 - A40 //assert ('A'+('01'..'40')).contains(eventType)
    assert ('2.2'..'2.6').contains(HL7version)

    def msg = creator.createGenericMessage(eventType, patient, HL7version)
    assert msg.PID[2].value == patient.nhsNumber
    assert msg.PID[5][1].value == patient.familyName
    assert msg.PID[7].value == patient.dateOfBirth

    answer = router.injectADTMessage(msg)
}

Then(~/we receive an ACK with "([^"]+)"/){ String ACK ->
    assert answer.MSA[1].value == ACK
}



