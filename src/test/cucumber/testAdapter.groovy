import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

/**
 * Created by gregorlenz on 24/09/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners([DependencyInjectionTestExecutionListener.class])
@ContextConfiguration(locations = ["/context.xml"])
class testAdapter extends CamelSpringTestSupport{

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/context.xml");
    }

    boolean sendMessage(String triggerEvent){
        if (triggerEvent == 'A01'){
            return true
        }
        return false
    }
}
