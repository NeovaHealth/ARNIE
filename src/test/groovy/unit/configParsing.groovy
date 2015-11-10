package unit

import junit.framework.TestSuite
import org.junit.Before
import org.junit.Test
import uk.co.neovahealth.ARNIE.configSupport

/**
 * Created by gregorlenz on 06/11/15.
 */
class configParsing extends TestSuite{
    def configFile

    @Before
    void setUp() {
        configFile = new configSupport()
    }

    @Test
    void odooTests() {
        assert configFile.values.odoo.hostname == 'localhost'
        assert configFile.values.odoo.port == 8069
    }

    @Test
    void routingSlips() {
        assert configFile.getRoutingSlips().get('A01') == "direct:updateOrCreatePatient, direct:admit"
        assert configFile.getRoutingSlips().get('A02') == "direct:updateOrCreatePatient, direct:transfer, direct:updateVisit"
        assert configFile.getRoutingSlips().get('A03') == "direct:updateOrCreatePatient, direct:discharge"
    }
}
