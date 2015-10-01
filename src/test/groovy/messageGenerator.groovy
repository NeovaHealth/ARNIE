import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v25.message.ADT_A02
import ca.uhn.hl7v2.model.v26.message.ADT_A03
import ca.uhn.hl7v2.parser.CustomModelClassFactory
import ca.uhn.hl7v2.parser.ModelClassFactory
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService

import static org.easymock.EasyMock.*

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

    Message createMessage(String event, Map values, String version){
        event = 'ADT_' + event
        Message msg = Message."$event"(version)

        msg.PID[2] = values.nhs_number
        msg.PID[4] = values.hospital_number
        msg.PID[5][1] = 'Simpson'
        msg.PID[5][2] = 'Homer'
        msg.PID[7] = values.dob
        msg.PID[8] = values.sex
        msg.PID[11][1] = values.address

        return msg
    }

    @Test
    void createGenericA01Message(){
        def valueMap = [nhs_number: '0123456789', hospital_number:'012345', family_name:'Simpson', given_name:'Homer', dob: '19801231000000', sex:'M', address: 'High Street', ward_admit:'06BN']
        def msg01 = createMessage('A01', valueMap, '2.2')

        assert msg01.getClass().is(ca.uhn.hl7v2.model.v22.message.ADT_A01)
        assert msg01.PID[2].value == valueMap.nhs_number
        assert msg01.PID[4].value == valueMap.hospital_number
        assert msg01.PID[5][1].value == 'Simpson'
        assert msg01.PID[5][2].value == 'Homer'
        assert msg01.PID[7].value == valueMap.dob
        assert msg01.PID[8].value == valueMap.sex
        assert msg01.PID[11][1].value == valueMap.address
    }

    @Test
    void createGenericA02Message(){
        def valueMap = [family_name:'Simpson', given_name:'Marge', address: 'High Street', location:'06BN']
        def msg02 = createMessage('A02', valueMap, '2.5')
        assert msg02.getClass().is(ADT_A02)
        assert msg02.PID[5][1].value == valueMap.family_name
        assert msg02.PID[5][2].value == valueMap.given_name
        assert msg02.PID[8].value == null
    }

    @Test
    void createGenericA03Message(){
        def valueMap = [family_name:'Simpson', given_name:'Bart', location:'06BN']
        def msg03 = createMessage('A03', valueMap, '2.6')
        assert msg03.getClass().is(ADT_A03)
        assert msg03.PID[5][2].value == 'Bart'
    }
}