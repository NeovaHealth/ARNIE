package cucumberSupport

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.ModelClassFactory
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService

import static org.easymock.EasyMock.*

/**
 * Created by gregorlenz on 30/09/15.
 */
class MessageCreation {

    public MessageCreation(){

        BidiMappingService mappingService = new BidiMappingService()
        //mappingService.addMappingScript(new ClassPathResource("example2.map"))
        ModelClassFactory mcf = new org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory()
        HapiContext context = new DefaultHapiContext(mcf)
        Registry registry = createMock(Registry)
        ContextFacade.setRegistry(registry)
        expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        expect(registry.bean(ModelClassFactory)).andReturn(mcf).anyTimes()
        expect(registry.bean(HapiContext)).andReturn(context).anyTimes()
        replay(registry)
    }

    Message createGenericMessage(String event, Patient patient, String version){
        Message msg = Message."ADT_$event"(version)

        msg.PID[2] = patient.nhsNumber
        msg.PID[4] = patient.hospitalNumber
        msg.PID[5][1] = patient.familyName
        msg.PID[5][2] = patient.givenName
        msg.PID[7] = patient.dateOfBirth
        msg.PID[8] = patient.sex
        msg.PID[11][1] = patient.address

        return msg
    }
}
