package support

import ca.uhn.hl7v2.model.Message
import org.apache.camel.ExchangePattern

/**
 * Created by gregorlenz on 16/09/15.
 */
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

class CamelConnector extends CamelSpringTestSupport{

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("classpath:context.xml");
    }

    @Override
    public String isMockEndpointsAndSkip(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:visitUpdate)|(direct:msgLogging)|(direct:updateOrCreatePatient))";
    }

    public Message injectADTMessage(Message msg) throws IOException, InterruptedException {
        def input  = msg.encode()

        //this is key to test routes of a camelContext properly
        this.setUp()

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit")
        admitEndpoint.expectedMessageCount(1)
        admitEndpoint.setResultWaitTime(3000)

        Message answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, input)
        template.sendBody("direct:msgLogging", input)

        admitEndpoint.assertIsSatisfied()
        assertMockEndpointsSatisfied()
        return answer
    }

    void testPostgres() {
        Resource input = new ClassPathResource("/msg-08.hl7")

        MockEndpoint msgHistory = getMockEndpoint("mock:direct:msgLogging")
        msgHistory.expectedMessageCount(1)

        template.sendBody("direct:msgLogging", input.getInputStream())
        assertMockEndpointsSatisfied()

    }


}
