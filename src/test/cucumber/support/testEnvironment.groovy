package support

import ca.uhn.hl7v2.model.Message
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

/**
 * Created by gregorlenz on 24/09/15.
 */
@RunWith(Cucumber.class)
class testEnvironment {
    Message answer

    def patient = new Patient()
    def router = new CamelConnector()
}
