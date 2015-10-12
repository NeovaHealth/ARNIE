package steps

import org.apache.camel.Endpoint
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import support.*
import org.openehealth.tutorial.ADTRouting

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.*

/**
 * Created by gregorlenz on 24/09/15.
*/
//@RunWith(Cucumber.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners([DependencyInjectionTestExecutionListener.class])
@ContextConfiguration(locations = ["/cucumber.xml"])
//@CucumberOptions(glue = {"cucumber.", "cucumber.api.spring"})
class testEnvironment extends CamelSpringTestSupport{
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(["/cucumber.xml"])
    }


    public testEnvironment(){
        /*
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
    */
        this.applicationContext = springContext
        //this.context = springContext
    }

    @Override
    public String isMockEndpointsAndSkip(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:visitUpdate)|(direct:msgLogging)|(direct:updateOrCreatePatient))";
    }

    def patient = new Patient()
    def router = new Router(springContext)


    @Autowired
    def springContext = new ClassPathXmlApplicationContext("cucumber.xml")

    @Autowired
    def testBean = springContext.getBean("testBean")

    @Autowired
    def routeBuilder = springContext.getBean("routeBuilder")

    @Autowired
    def BDDcamelContext = springContext.getBean("camelContext")

    @Autowired
    ProducerTemplate producer = BDDcamelContext.createProducerTemplate()

    //@Autowired
    //MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit")

    @Autowired
    MockEndpoint listenerEndpoint = MockEndpoint.resolve(BDDcamelContext, "mock:direct:hl7listener")

    @Autowired
    MockEndpoint routerEndpoint = MockEndpoint.resolve(BDDcamelContext, "mock:direct:hl7router")


}

World() {
    new testEnvironment()
}

Before() {
    ARNIE = new ADTRouting()

    creator = new MessageCreation()

}

Given(~/Patient "([^"]+)", born on "([^"]+)" with NHS number "([^"]+)" is admitted to ward "([^"]+)"./) { String patientName, String dob, String nhs_number, String ward ->

    assert testBean.getClass() == Patient
    assert routeBuilder.getClass() == ADTRouting

    router.testA01("Test")
    //MockEndpoint transferEndpoint = getMockEndpoint("direct:transfer")
    //transferEndpoint.expectedMessageCount(0)
    admitEndpoint.setExpectedMessageCount(1);

    producer.sendBody(admitEndpoint, "Hello")
    admitEndpoint.assertIsSatisfied()

    producer.sendBody(listenerEndpoint, "Hello")
    routerEndpoint.setExpectedMessageCount(1)
    routerEndpoint.assertIsSatisfied()

    patient.with {
        familyName = patientName
        dateOfBirth = dob
        nhsNumber = nhs_number
        admitLocation = ward
    }

    //assert testBean != null
    assert patient.familyName == patientName
    assert !patient.dateOfBirth.contains(' ')
    assert patient.nhsNumber.isNumber() && patient.nhsNumber.length() == 10
    assert patient.admitLocation == ward
}

When(~/an "([^"]+)" message using HL7 Version "([^"]+)" is sent to ARNIE with the name in the following field: "([^"]+)"/){ eventType, HL7version, nameField ->
    //TODO add closure to iterate over A01 - A40 //assert ('A'+('01'..'40')).contains(eventType)
    assert ('2.2'..'2.6').contains(HL7version)

    def msg = creator.createGenericMessage(eventType, patient, HL7version)
    assert msg.PID[2].value == patient.nhsNumber
    assert msg.PID[5][1].value == patient.familyName
    assert msg.PID[7].value == patient.dateOfBirth

    template.sendBody("direct:hl7listener", msg)
    router.testA01(msg)
}

Then(~/we receive an ACK with "([^"]+)"/){ String ACK ->
    throw new cucumber.api.PendingException()
}



