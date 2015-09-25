package steps

import org.openehealth.tutorial.ADTRouting
import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.*

/**
 * Created by gregorlenz on 24/09/15.
*/

class testEnvironment {
    def status

    String customMethod(){
        "foo"
    }
    boolean verifier(String name){
        if (name == 'Gandalf'){
            return true
        }
        false
    }
    boolean returntrue() {true}
}

World {
    new testEnvironment()
}

Before() {
    ARNIE = new ADTRouting()
}

Given(~/Patient "(\w+)" is admitted to ward "6BN"/) { String patientName ->
    assert verifier(patientName) == true
}

When(~/an "(\w+)" message is sent to ARNIE/){
    status = returntrue()
}

Then(~/we receive an ACK with "(\w+)"/){ String ACK ->
    assert verifier(ACK) == true
}



