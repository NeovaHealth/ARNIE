package integration

import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Test
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

/**
 * Created by gregorlenz on 17/11/15.
 */
class APItest extends CamelSpringTestSupport {

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/context.xml")
    }

    @Test
    void registerCall() {
        //Resource input = new ClassPathResource("/msg-01.hl7")


    }
}
