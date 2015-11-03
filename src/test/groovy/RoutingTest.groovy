/**
 * Created by gregorlenz on 16/09/15.
 */
import ca.uhn.hl7v2.model.Message
import MessageGenerator
import cucumber.api.java.Before
import org.apache.camel.ExchangePattern
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringTestSupport
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
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:updateVisit)|(direct:msgLogging)|(direct:updateOrCreatePatient))";
    }

    @Test
    public void testA01() throws IOException, InterruptedException {
        //Resource input  = new ClassPathResource("/msg-01.hl7")
        def gen = new MessageGenerator()

        Patient admitPatient = new Patient(nhsNumber: '0123456789', hospitalNumber:'012345', familyName:'Simpson',
                givenName:'Homer', dateOfBirth: '19801231000000', sex:'M', address: 'High Street', admitLocation: '06BN')
        def msg01 = gen.createMessage('A01', admitPatient, '2.2')

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit")
        admitEndpoint.expectedMessageCount(1)

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer")
        transferEndpoint.expectedMessageCount(0)

        Message answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, msg01.encode())

        assert answer.MSA[1].value == 'AA'
        assertMockEndpointsSatisfied()
    }

    @Test
    void testA08() throws InterruptedException, IOException {
        //Resource input = new ClassPathResource("/msg-08.hl7");
        def gen = new MessageGenerator()

        Patient updatePatient = new Patient(nhsNumber: '0123456789')
        def msg08 = gen.createMessage('A08', updatePatient, '2.2')

        MockEndpoint visitUpdateEndpoint = getMockEndpoint("mock:direct:updateVisit");
        visitUpdateEndpoint.expectedMessageCount(1);

        MockEndpoint patientUpdateEndpoint = getMockEndpoint("mock:direct:updateOrCreatePatient")
        patientUpdateEndpoint.expectedMessageCount(1)

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(0);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(0);

        Message answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, msg08.encode())

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

    //@Test
    void messageGeneratorTest() {
        assert MessageGenerator.getA01().getClass().is(Message)

    }

    //@Test
    void msgGeneratorTest(){
        Message thisA01 = MessageGenerator.getA01()
        assert thisA01.getClass().is(Message)
    }

}
