package integration

import ca.uhn.hl7v2.model.Message
import org.apache.camel.ExchangePattern
import org.apache.camel.component.mock.MockEndpoint

/**
 * Created by gregorlenz on 16/09/15.
 */
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import support.MessageGenerator
import support.Patient

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners([DependencyInjectionTestExecutionListener.class])
@ContextConfiguration(locations = ["/context.xml"])
class RoutingTest extends CamelSpringTestSupport{

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/context.xml");
    }

    @Override
    public String isMockEndpoints(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|" +
                "(direct:updateVisit)|(direct:updateOrCreateVisit)|(direct:updateOrCreatePatient)|(direct:register))";
    }

    MessageGenerator gen

    Random random = new Random()
    def hospNumb, nhsNumb = ''
    Patient dummy1, dummy2

    MockEndpoint admitEndpoint, transferEndpoint, dischargeEndpoint, registerEndpoint, visitUpdateEndpoint, patientUpdateEndpoint
    Message answer

    def shuffle = {
        if (!it) {  //Groovy truth: it == 0 is false
            hospNumb = random.nextInt(10).toString()
        } else {hospNumb += random.nextInt(10).toString()}
    }

    @Before
    void setUp() {
        super.setUp()

        gen = new MessageGenerator()

        6.times(shuffle) //create new random Hospital Number with 6 digits

        dummy1 = new Patient(nhsNumber: 1223334444, hospitalNumber: hospNumb, familyName: 'Dummy', givenName: 'Dillon', dateOfBirth: '19441231090000', sex:'M', address: 'Main Street', admitLocation: '08BS')
        admitEndpoint = getMockEndpoint("mock:direct:admit")
        transferEndpoint = getMockEndpoint("mock:direct:transfer")
        registerEndpoint = getMockEndpoint("mock:direct:register")
        dischargeEndpoint = getMockEndpoint("mock:direct:discharge")
        visitUpdateEndpoint = getMockEndpoint("mock:direct:updateVisit")
        patientUpdateEndpoint = getMockEndpoint("mock:direct:updateOrCreatePatient")
    }


    @Test
    void testA01() throws IOException, InterruptedException {
        //Resource input  = new ClassPathResource("/msg-01.hl7")

        Patient admitPatient = new Patient(nhsNumber: '0123456789', hospitalNumber:'012345', familyName:'Simpson',
                givenName:'Homer', dateOfBirth: '19801231000000', sex:'M', address: 'High Street', admitLocation: '06BN')
        def msg01 = gen.createMessage('A01', admitPatient, '2.2')
        admitEndpoint.expectedMessageCount(1)
        patientUpdateEndpoint.expectedMessageCount(1)
        transferEndpoint.expectedMessageCount(0)

        answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, msg01.encode())

        assert dummy1.getClass() == Patient
        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

    @Test
    void testA02() {
        def msg02 = gen.createMessage('A02', dummy1, '2.5')
        patientUpdateEndpoint.expectedMessageCount(1)
        transferEndpoint.expectedMessageCount(1)
        visitUpdateEndpoint.expectedMessageCount(1)
        admitEndpoint.expectedMessageCount(0)

        answer = template.requestBody("direct:hl7listener", msg02.encode())

        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

    @Test
    void testA03() {
        def msg03 = gen.createMessage('A03', dummy1, '2.2')
        patientUpdateEndpoint.expectedMessageCount(1)
        dischargeEndpoint.expectedMessageCount(1)
        admitEndpoint.expectedMessageCount(0)
        transferEndpoint.expectedMessageCount(0)

        answer = template.requestBody("direct:hl7listener", msg03.encode())

        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

    @Test
    void testA28() {
        def msg28 = gen.createMessage('A28', dummy1, '2.4')
        registerEndpoint.expectedMessageCount(1)
        admitEndpoint.expectedMessageCount(0)
        transferEndpoint.expectedMessageCount(0)

        answer = template.requestBody("direct:hl7listener", msg28.encode())

        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

    @Test
    void testA31() {
        def msg31 = gen.createMessage('A31', dummy1, '2.4')
        patientUpdateEndpoint.expectedMessageCount(1)
        admitEndpoint.expectedMessageCount(0)
        transferEndpoint.expectedMessageCount(0)
        dischargeEndpoint.expectedMessageCount(0)

        answer = template.requestBody("direct:hl7listener", msg31.encode())

        assert  answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

}
