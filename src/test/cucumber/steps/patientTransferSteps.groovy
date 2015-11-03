package steps

import ca.uhn.hl7v2.model.Message
import cucumber.api.PendingException
import support.*

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before
import static cucumber.api.groovy.Hooks.World

/**
 * Created by gregorlenz on 16/10/15.
 */

class testEnv02 {
    Message answer02

    def patient02 = new Patient()

    def router02 = new CamelConnector()
}

World() {
    new testEnv02()
}

Before() {
    creator = new MessageCreation()
}

Given(~/Patient "([^"]+)", who has already been admitted, has to be transferred to another ward "([^"]+)"./){ ->
    //TODO check if Patient 'name' is already admitted
    assert patient02.address != null
    throw new PendingException()
}

When(~/an "([^"]+)" message using HL7 version "([^"]+)" is sent to ARNIE with the target location in "([^"]+)"/) { ->
    throw new PendingException()
}

Then(~/an Acknowledgement message for A02 with "([^"]+)" is received./){ String ACK ->
    //assert answer01.getClass() == Message
    //.getParent()?
    //assert answer01.getMessageType().toString() == 'ACK'
    assert answer02.MSA[1].value == ACK
}