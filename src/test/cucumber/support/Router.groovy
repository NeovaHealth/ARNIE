package support
/**
 * Created by gregorlenz on 16/09/15.
 */
import ca.uhn.hl7v2.model.Message
import cucumber.api.junit.Cucumber
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.impl.DefaultConsumerTemplate
import org.apache.camel.impl.DefaultProducerTemplate
import org.apache.camel.spring.CamelBeanPostProcessor
import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner
import org.apache.camel.test.spring.CamelSpringTestHelper
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

//@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners([DependencyInjectionTestExecutionListener.class])
//@ContextConfiguration(locations = ["/context.xml"], inheritLocations = false, inheritInitializers = false)
class Router extends CamelSpringTestSupport{

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("classpath:context.xml");
    }

    @Override
    public String isMockEndpointsAndSkip(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:visitUpdate)|(direct:msgLogging)|(direct:updateOrCreatePatient))";
    }

    /*@Autowired
    private ApplicationContext applicationContext*/

    public void testA01(Message msg) throws IOException, InterruptedException {
        def input  = msg.encode()
        Resource input1  = new ClassPathResource("/msg-01.hl7")

        def springcontext = new ClassPathXmlApplicationContext("classpath:context.xml")

        def methodCamelContext = springcontext.getBean("camelContext")

        this.applicationContext = springcontext
        this.context = methodCamelContext
        //this.createCamelContext()



        this.setUp()
        //this.context = methodCamelContext

        //CamelSpring or CamelTest support class? setup


        MockEndpoint admitEndpoint1 = MockEndpoint.resolve(context, "mock:direct:admit")
        //MockEndpoint admitEndpoint1 = getMockEndpoint("mock:direct:admit")
        admitEndpoint1.expectedMessageCount(1)
        admitEndpoint1.setResultWaitTime(3000)
        //ProducerTemplate ptemplate = context.createProducerTemplate()
        //ptemplate.sendBody("direct:hl7listener", input1.getInputStream())

        //template = new DefaultProducerTemplate(methodCamelContext)
        //template.start()
        template.sendBody("direct:hl7listener", input1.getInputStream())

        log.info("Sent A01")
        admitEndpoint1.assertIsSatisfied()

        //assertMockEndpointsSatisfied()
    }

    void testA08() throws InterruptedException, IOException {
        Resource input = new ClassPathResource("/msg-08.hl7");

        MockEndpoint visitUpdateEndpoint = getMockEndpoint("mock:direct:visitUpdate");
        visitUpdateEndpoint.expectedMessageCount(1);

        MockEndpoint patientUpdateEndpoint = getMockEndpoint("mock:direct:updateOrCreatePatient")
        patientUpdateEndpoint.expectedMessageCount(1)

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(0);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(0);

        template.sendBody("direct:hl7listener", input.getInputStream());
        assertMockEndpointsSatisfied();
    }

    void testPostgres() {
        Resource input = new ClassPathResource("/msg-08.hl7")

        MockEndpoint msgHistory = getMockEndpoint("mock:direct:msgLogging")
        msgHistory.expectedMessageCount(1)

        template.sendBody("direct:msgLogging", input.getInputStream())
        assertMockEndpointsSatisfied()

    }

    //@Test
    void messageGeneratorTest() {
        assert messageGenerator.getA01().getClass().is(Message)

    }

    //@Test
    void msgGeneratorTest(){
        Message thisA01 = messageGenerator.getA01()
        assert thisA01.getClass().is(Message)
    }

}
