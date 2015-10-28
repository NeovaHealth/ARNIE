package steps

import ca.uhn.hl7v2.model.Message
import cucumber.api.PendingException
import support.*

import static cucumber.api.groovy.Hooks.Before
import static cucumber.api.groovy.Hooks.World
import static cucumber.api.groovy.EN.*

/**
 * Created by gregorlenz on 16/10/15.
 */

class testEnv03 {
    Message answer03

    def patient03 = new Patient()

    def router03 = new CamelConnector()
}

World() {
    new testEnv03()
}

Before() {

}

Given(~/Patient "([^"]+)", who has already been admitted, should be discharged./){ name ->
    //TODO check if Patient 'name' is already admitted
    throw new PendingException()
}

When(~/an A03 message using HL7 version "([^"]+)" is sent to ARNIE./) {
    triggerEvent, version ->

    throw new PendingException()
}

Given(~/the eObs API is ready/) {
    steps.patientDischargeSteps

}
