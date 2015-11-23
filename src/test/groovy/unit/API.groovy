package unit

import ca.uhn.hl7v2.model.Message
import junit.framework.TestSuite
import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.DefaultExchange
import org.junit.After
import org.junit.Before
import org.junit.Test
import support.MessageGenerator
import support.Patient
import uk.co.neovahealth.ARNIE.eObsCalls

/**
 * Created by gregorlenz on 06/11/15.
 */
class API extends TestSuite{
    private caller
    private Exchange exchange
    private MessageGenerator gen = new MessageGenerator()
    private Message message
    private Patient dummy = new Patient()
    private CamelContext ctx

    @Before
    void setUp() {
        caller = new eObsCalls()
        assert caller.login()
        ctx = new DefaultCamelContext();
        exchange = new DefaultExchange(ctx);
    }

    @After
    void tearDown() {
        //delete patient data
    }

    @Test
    void visitExists() {
        assert caller.visitExists()
    }

    @Test
    void patientExist() {
        assert caller.patientExists()
    }

    @Test
    void patientRegister() {
        dummy.hospitalNumber = '12'
        message =  gen.createMessage('A01', dummy, '2.4')
        exchange.in.body = message
        assert caller.patientRegister(exchange)
    }

    @Test
    void patientAdmit() {
        assert caller.patientAdmit()
    }

    @Test
    void patientTransfer() {
        assert caller.patientTransfer()
    }

    @Test
    void patientDischarge() {
        dummy.hospitalNumber = '12'
        message = gen.createMessage('A03', dummy, '2.4')
        exchange.in.body = message
        assert caller.patientDischarge(exchange)
    }

    @Test
    void patientNew() {
        assert caller.patientNew()
    }

    @Test
    void patientUpdate() {
        assert caller.patientUpdate()
    }
}
