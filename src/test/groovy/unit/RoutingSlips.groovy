package unit

import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Before
import org.junit.Test
import org.openehealth.tutorial.ComputeRoutingSlip
import support.MessageGenerator
import support.Patient

/**
 * Created by gregorlenz on 09/11/15.
 */
class RoutingSlips {
    def computer
    def message
    def gen

    @Before
    void setUp() {
        computer = new ComputeRoutingSlip()
        gen = new MessageGenerator()
    }

    @Test
    void A01test() {
        message = gen.createMessage('A01', new Patient(), '2.4' )
        assert computer.messageSteps(message) == "direct:updateOrCreatePatient, direct:admit"
    }
}
