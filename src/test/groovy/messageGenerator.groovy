import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v25.message.ADT_A02
import ca.uhn.hl7v2.parser.CustomModelClassFactory
import ca.uhn.hl7v2.parser.ModelClassFactory
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService

import static org.easymock.EasyMock.*
import static org.junit.Assert.assertEquals


/**
 * Created by gregorlenz on 23/09/15.
 */
public class messageGenerator {

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

    Message getA01(String version) {
        return Message.ADT_01(version)
    }

    @Test
    void makeMsg(){
        Message newA01 = getA01('2.5')
        assertEquals 'ADT', newA01.MSH.messageType.messageCode.value
        assertEquals '2.5', newA01.MSH.versionID.versionID.value
    }

    @Test
    void messageFromScratch(){
        Message msg = Message.ADT_A01('2.4')
        msg.MSH.with {
            sendingApplication.namespaceID.value = 'TestSendingSystem'
            sequenceNumber.value = '123'
        }
        msg.PID.with {
            getPatientName(0).familyName.surname.value = 'Doe'
            getPatientName(0).givenName.value = 'John'
            getPatientIdentifierList(0).ID.value = '123456'
        }
        assert msg.PID[3].value == '123456'
        assert msg.PID[5][1].value == 'Doe'
        assert msg.PID[5][2].value == 'John'
    }

    Message createMessage(String event, String name, String version){
        Message msg = Message."$event"(version)
        msg.PID.with {
            getPatientName(0).familyName.surname.value = name
        }
        return msg
    }

    @Test
    void createGenericMessage(){
        def msg01 = createMessage('ADT_A01', 'Homer', '2.4')
        assert msg01.getClass().is(ca.uhn.hl7v2.model.v24.message.ADT_A01)

        def msg02 = createMessage('ADT_A02', 'Marge', '2.5')
        assert msg02.getClass().is(ADT_A02)
    }
}