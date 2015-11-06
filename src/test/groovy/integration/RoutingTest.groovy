package integration

import ca.uhn.hl7v2.model.Message

/**
 * Created by gregorlenz on 16/09/15.
 */
import org.apache.camel.ExchangePattern
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
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
    public String isMockEndpointsAndSkip(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:updateVisit)|(direct:updateOrCreatePatient))";
    }

    MessageGenerator gen

    Patient dummy1, dummy2

    MockEndpoint admitEndpoint, transferEndpoint, dischargeEndpoint, visitUpdateEndpoint, patientUpdateEndpoint
    Message answer

    @Before
    void setUp() {
        super.setUp()

        gen = new MessageGenerator()

        dummy1 = new Patient(nhsNumber: 1223334444, familyName: 'Dummy', givenName: 'Dillon', dateOfBirth: '19441231090000', sex:'M', address: 'Main Street', admitLocation: '08BS')
        admitEndpoint = getMockEndpoint("mock:direct:admit")
        transferEndpoint = getMockEndpoint("mock:direct:transfer")
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
        transferEndpoint.expectedMessageCount(0)

        answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, msg01.encode())

        assert dummy1.getClass() == Patient
        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

    @Test
    void testA02() {

    }

    @Test
    void testErrorHandling() {
        Resource input = new ClassPathResource("/msg-ERR.hl7")

        template.sendBody("direct:hl7listener", input.getInputStream())

        assert input == new ClassPathResource("target/output/*")
    }


    @Test
    void testA08() throws InterruptedException, IOException {
        //Resource input = new ClassPathResource("/msg-08.hl7");

        Patient updatePatient = new Patient(nhsNumber: '0123456789')
        def msg08 = gen.createMessage('A08', updatePatient, '2.2')

        visitUpdateEndpoint.expectedMessageCount(1);
        patientUpdateEndpoint.expectedMessageCount(1)
        admitEndpoint.expectedMessageCount(0);
        transferEndpoint.expectedMessageCount(0);

        answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, msg08.encode())

        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied();
    }

    @Test
    void testPostgres() {
        Resource input = new ClassPathResource("/msg-08.hl7")

        MockEndpoint msgHistory = getMockEndpoint("mock:direct:msgLogging")
        msgHistory.expectedMessageCount(1)

        template.sendBody("direct:msgLogging", input.getInputStream())
        assertMockEndpointsSatisfied()
    }

}
