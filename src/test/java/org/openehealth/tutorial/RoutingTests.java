package org.openehealth.tutorial;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.IOException;

/**
 * Created by gregorlenz on 31/08/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = { "/context.xml" })
public class RoutingTests extends CamelSpringTestSupport {

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/context.xml");
    }

    //@Autowired
    //private ProducerTemplate producerTemplate;

    @Override
    public String isMockEndpointsAndSkip(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:visitUpdate))";
    }

    //@Test
    public void testRoutingSlip1() throws InterruptedException {
        int message = 1;

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(1);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(0);

        MockEndpoint dischargeEndpoint = getMockEndpoint("mock:direct:discharge");
        dischargeEndpoint.expectedMessageCount(0);

        MockEndpoint updatePatientEndpoint = getMockEndpoint("mock:direct:updatePatient");
        updatePatientEndpoint.expectedMessageCount(1);

        template.sendBody("direct:hl7listener", message);
        assertMockEndpointsSatisfied();
    }

    //@Test
    public void testRoutingSlip2() throws InterruptedException {
        int message = 2;

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(0);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(1);

        MockEndpoint dischargeEndpoint = getMockEndpoint("mock:direct:discharge");
        dischargeEndpoint.expectedMessageCount(0);

        MockEndpoint updatePatientEndpoint = getMockEndpoint("mock:direct:updatePatient");
        updatePatientEndpoint.expectedMessageCount(1);

        template.sendBody("direct:hl7listener", message);

        System.out.println(testMessages.getTestA01());
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testA01() throws IOException, InterruptedException {
        Resource input  = new ClassPathResource("/msg-01.hl7");
        //def messageString = mak
        //String input = testMessages.getTestA01();
        //log.info(input);

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(1);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(0);

        template.sendBody("direct:hl7listener", input.getInputStream());
        log.info("Sent A01");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testA08() throws InterruptedException, IOException {
        Resource input = new ClassPathResource("/msg-08.hl7");

        MockEndpoint visitUpdateEndpoint = getMockEndpoint("mock:direct:visitUpdate");
        visitUpdateEndpoint.expectedMessageCount(1);

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(0);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(0);

        template.sendBody("direct:hl7listener", input.getInputStream());
        assertMockEndpointsSatisfied();
    }



    /*
    //@Test
    public void MockTest(){
    //    MockEndpoint resultEndpoint = context.
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:topic:quote").to("mock:quote");
            }
        };
    }
    @Test
    public void testQuote() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedMessageCount(1);
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }
    */

}
