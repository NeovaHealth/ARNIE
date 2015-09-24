package steps

import static cucumber.api.groovy.EN.*

/**
 * Created by gregorlenz on 24/09/15.
 */
class patientAdmitSteps {
    //foo
    def result

    String customMethod(){}
}

World {
    new steps.patientAdmitSteps()
}

Given(~/Patient "(\w+)" is admitted to ward "6BN"/) { String patientName ->
    createMessage($patientName, '2.4')
}

When(~/an "(\w+)" message is sent to ARNIE/){ String triggerEvent ->
    boolean status = sendMessage($triggerEvent)
}

Then(~/we receive an ACK with "AA"/){
    assert status == true
}



