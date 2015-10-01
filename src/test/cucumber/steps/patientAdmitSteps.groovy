package steps

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.CustomModelClassFactory
import ca.uhn.hl7v2.parser.ModelClassFactory
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService
import support.*
import org.openehealth.tutorial.ADTRouting
import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.*
import static org.easymock.EasyMock.createMock
import static org.easymock.EasyMock.expect
import static org.easymock.EasyMock.replay

/**
 * Created by gregorlenz on 24/09/15.
*/

class testEnvironment {
    def patient = new Patient()
}

World {
    new testEnvironment()
}

Before() {
    ARNIE = new ADTRouting()
    creator = new MessageCreation()

    BidiMappingService mappingService = new BidiMappingService()
    //mappingService.addMappingScript(new ClassPathResource("example2.map"))
    ModelClassFactory mcf = new CustomModelClassFactory()
    HapiContext context = new DefaultHapiContext(mcf)
    Registry registry = createMock(Registry)
    ContextFacade.setRegistry(registry)
    expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
    expect(registry.bean(ModelClassFactory)).andReturn(mcf).anyTimes()
    expect(registry.bean(HapiContext)).andReturn(context).anyTimes()
    replay(registry)
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
}

When(~/an "(\w+)" message is sent to ARNIE/){

    def msg = creator.createGenericMessage('A01', patient, '2.2')
    assert msg.getClass().is(ca.uhn.hl7v2.model.v22.message.ADT_A01)
    assert msg.PID[2].value == nhs_number
    assert msg.PID[5][1].value == patientName
    assert msg.PID[7].value == dob


    throw new cucumber.api.PendingException()
}

Then(~/we receive an ACK with "(\w+)"/){ String ACK ->
    throw new cucumber.api.PendingException()
}



