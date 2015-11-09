package org.openehealth.tutorial

import ca.uhn.hl7v2.model.Message
import org.apache.camel.RoutingSlip

/**
 * Created by gregorlenz on 10/09/15.
 */
public class ComputeRoutingSlip {
    def configParser = new configSupport()

    @RoutingSlip
    String messageSteps(Message inboundMsg) {

        String msgTrigger = inboundMsg.getTriggerEvent()

        //return configParser.getRoutingSlips().get(msgTrigger)

        switch (msgTrigger){
            case 'A01': return "direct:updateOrCreatePatient, direct:admit"
            case 'A02': return "direct:updateOrCreatePatient, direct:transfer"
            case 'A03': return "direct:updateOrCreatePatient, direct:discharge"
            case 'A08': return "direct:updateOrCreatePatient, direct:updateVisit"
            case 'A11': return "direct:updateOrCreatePatient, direct:cancelAdmit"
            case 'A12': return "direct:updateOrCreatePatient, direct:cancelTransfer"
            case 'A13': return "direct:updateOrCreatePatient, direct:cancelDischarge"
            case 'A31': return "direct:updateOrCreatePatient"
            default: return
        }
    }

}