package support

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.CustomModelClassFactory
import ca.uhn.hl7v2.parser.ModelClassFactory
import org.easymock.EasyMock
import org.junit.BeforeClass
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService

import static org.easymock.EasyMock.createMock
import static org.easymock.EasyMock.expect
import static org.easymock.EasyMock.expect
import static org.easymock.EasyMock.expect
import static org.easymock.EasyMock.replay

/**
 * Created by gregorlenz on 30/09/15.
 */
class MessageCreation {

    public MessageCreation(){

        BidiMappingService mappingService = new BidiMappingService()
        //mappingService.addMappingScript(new ClassPathResource("example2.map"))
        ModelClassFactory mcf = new org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory()
        HapiContext context = new DefaultHapiContext(mcf)
        Registry registry = EasyMock.createMock(Registry)
        ContextFacade.setRegistry(registry)
        EasyMock.expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        EasyMock.expect(registry.bean(ModelClassFactory)).andReturn(mcf).anyTimes()
        EasyMock.expect(registry.bean(HapiContext)).andReturn(context).anyTimes()
        EasyMock.replay(registry)
    }
    @BeforeClass
    static void setUp() {
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

    //
    Message createGenericMessage(String event, Patient patient, String version){
        event = 'ADT_' + event
        Message msg = Message."$event"(version)

        msg.PID[2] = patient.nhsNumber
        msg.PID[4] = patient.hospitalNumber
        msg.PID[5][1] = patient.familyName
        msg.PID[5][2] = patient.givenName
        msg.PID[7] = patient.dateOfBirth
        msg.PID[8] = patient.sex
        msg.PID[11][1] = patient.address


        /*
        msg.PID.with {
            getPatientID().getIDNumber().value = values.nhs_number //2
            getAlternatePatientIDPID(0).getIDNumber().value = values.hospital_number //4
            getPatientName(0).getFamilyName().getSurname().value = values.family_name //5-1
            getPatientName(0).getGivenName().value = values.given_name //5-2
            getDateTimeOfBirth().getTime().value = values.dob //7
            getAdministrativeSex().value = values.sex //8
            getPatientAddress(0).getStreetAddress().getStreetName().value = values.address //11-1-2
        }
        */
        return msg
    }
}
