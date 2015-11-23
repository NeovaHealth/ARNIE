package uk.co.neovahealth.ARNIE

import org.apache.camel.Exchange

/**
 * Created by gregorlenz on 23/11/15.
 */
class ADTProcessing {
    void getMapFromFields(Exchange exchange) {

    }

    def getHospitalNumber(Exchange exchange) {
        exchange.in.body.PID[4][1].value
    }
}
