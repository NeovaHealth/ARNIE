package org.openehealth.tutorial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;

//@RunWith(SpringJUnit4ClassRunner.class)
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
//@ContextConfiguration(locations = { "/context.xml" })
public class SampleRouteTest {

    //@Autowired
    //private ProducerTemplate producerTemplate;
    
    @Before
    public void setUp(){
        //something happens beforehand
    }

    @After
    public void tearDown(){
        //something happens afterwards
    }

    /*
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Test
    public void ackTest() throws IOException, InterruptedException{
        Resource input = new ClassPathResource("/msg-01.hl7");
        producerTemplate.sendBody("direct:input", input.getInputStream());
        resultEndpoint.expectedBodiesReceived("ACK");
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals("abcabc", producerTemplate.requestBody("direct:inputMultiply", "abc"));
    }
    
    //@Test
    public void testReverse() throws Exception {
        assertEquals("cba", producerTemplate.requestBody("direct:inputReverse", "abc"));
    }

    //@Test
    public void testMessageReverse() throws IOException {
        Resource input = new ClassPathResource("/msg-01.hl7");
        producerTemplate.sendBody("direct:inputReverse", input.getInputStream().toString());
        String result = new StringBuilder(new ClassPathResource("target/output/file.reverse").toString()).reverse().toString();
        assertEquals(input.toString(), result);
    }

    @Test
    public void testRoute() throws IOException, HL7Exception {
        Resource input = new ClassPathResource("/msg-01.hl7");
        producerTemplate.sendBody("direct:input", input.getInputStream());
        Resource result = new FileSystemResource("target/output/HZL.hl7");
        assertEquals(
                load(getClass().getResourceAsStream("/msg-01.hl7.expected")).toString(),
                load(result.getInputStream()).toString());
    }

    protected static <T extends Message> T load(InputStream is) throws HL7Exception {
        return (T)new PipeParser().parse(
                new Scanner(is).useDelimiter("\\A").next());
    }
    */

}
