package steps

import cucumber.api.PendingException
import support.*

import static cucumber.api.groovy.Hooks.Before
import static cucumber.api.groovy.Hooks.World
import static cucumber.api.groovy.EN.*

/**
 * Created by gregorlenz on 16/10/15.
 */

World() {
    new testEnvironment()
}

Before() {

}

Given(~/Patient "([^"]+)", who has already been admitted, has to be transferred to another ward "([^"]+)"./){ name, location ->
    //TODO check if Patient 'name' is already admitted
    throw new PendingException()
}

When(~/an "([^"]+)" message using HL7 version "([^"]+)" is sent to ARNIE with the target location in "([^"]+)"/) {
    triggerEvent, version, field ->

    throw new PendingException()
}