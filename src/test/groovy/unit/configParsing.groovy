package unit

import org.junit.Before
import org.junit.Test
import org.openehealth.tutorial.configSupport

/**
 * Created by gregorlenz on 06/11/15.
 */
class configParsing {
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
        assert configFile.getRoutingSlips().get('A01').toString() == "direct:updateOrCreatePatient, direct:admit"
        assert configFile.getRoutingSlips().get('A02').toString() == "direct:updateOrCreatePatient, direct:transfer, direct:updateVisit"
    }
}
