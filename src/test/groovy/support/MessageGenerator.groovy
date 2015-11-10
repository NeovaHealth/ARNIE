package support

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v22.segment.PV1
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
public class MessageGenerator {

    public MessageGenerator(){
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

    Message createMessage(String event, Patient patient, String version){
        Message msg = Message."ADT_$event"(version)

        msg.with {
            PID[2] = patient.nhsNumber
            PID[4] = patient.hospitalNumber
            PID[5][1] = patient.familyName
            PID[5][2] = patient.givenName
            PID[7] = patient.dateOfBirth
            PID[8] = patient.sex
            PID[11][1] = patient.address
        }
        return msg
    }

    @Test
    void createGenericA01Message(){
        Patient admitPatient = new Patient(nhsNumber: '0123456789', hospitalNumber:'012345', familyName:'Simpson',
                givenName:'Homer', dateOfBirth: '19801231000000', sex:'M', address: 'High Street', admitLocation: '06BN')
        def msg01 = createMessage('A01', admitPatient, '2.2')

        assert msg01.getClass().is(ca.uhn.hl7v2.model.v22.message.ADT_A01)
        assert msg01.PID[2].value == admitPatient.nhsNumber
        assert msg01.PID[4].value == admitPatient.hospitalNumber
        assert msg01.PID[5][1].value == 'Simpson'
        assert msg01.PID[5][2].value == 'Homer'
        assert msg01.PID[7].value == admitPatient.dateOfBirth
        assert msg01.PID[8].value == admitPatient.sex
        assert msg01.PID[11][1].value == admitPatient.address
    }

    @Test
    void createGenericA02Message() {
        Patient transferPatient = new Patient(familyName:'Simpson', givenName:'Marge', address: 'High Street', admitLocation: '06BN')
        def msg02 = createMessage('A02', transferPatient, '2.5')

        assert msg02.getClass().is(ca.uhn.hl7v2.model.v25.message.ADT_A02)
        assert msg02.PID[5][1].value == transferPatient.familyName
        assert msg02.PID[5][2].value == transferPatient.givenName
        assert msg02.PID[8].value == 'null'
        assert msg02.PID[11][1].value == transferPatient.address
    }

    @Test
    void createGenericA03Message() {
        Patient dischargePatient = new Patient(familyName:'Simpson', givenName:'Bart', admitLocation: '06BN')
        def msg03 = createMessage('A03', dischargePatient, '2.6')
        assert msg03.getClass().is(ca.uhn.hl7v2.model.v26.message.ADT_A03)
        assert msg03.PID[5][2].value == 'Bart'
    }

    @Test
    void createGenericA31Message() {
        Patient updatePatient = new Patient(familyName: 'Simpson', givenName: 'Barto', address: 'Down Street')
        def msg31 = createMessage('A31', updatePatient, '2.3')
        assert msg31.getClass().is(ca.uhn.hl7v2.model.v23.message.ADT_A31)
        assert msg31.PID[5][1].value == updatePatient.familyName
        assert msg31.PID[5][2].value == updatePatient.givenName
        assert msg31.PID[11][1].value == updatePatient.address
    }
}