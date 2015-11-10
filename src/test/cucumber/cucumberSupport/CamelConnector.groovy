package cucumberSupport

import ca.uhn.hl7v2.model.Message
import org.apache.camel.ExchangePattern

/**
 * Created by gregorlenz on 16/09/15.
 */
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

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

        Message answer = template.sendBody("direct:hl7listener", ExchangePattern.InOut, input)
        template.sendBody("direct:msgLogging", input)

        return answer
    }

}
