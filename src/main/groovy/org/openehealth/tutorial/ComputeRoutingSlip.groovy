package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import org.apache.camel.RoutingSlip

/**
 * Created by gregorlenz on 10/09/15.
 */
public class ComputeRoutingSlip {

    @RoutingSlip
    String messageSteps(Message inflightmsg) {

        String msgTrigger = inflightmsg.getTriggerEvent()

        //HapiContext hapiContext = new DefaultHapiContext()

        //def msg = Message.ADT_A01(hapiContext, '2.5')

        switch (msgTrigger){
            case 'A01': return "direct:admit"
            case 'A02': return "direct:transfer"
            case 'A03': return "direct:discharge"
            case 'A08': return "direct:visitUpdate"
            default: return
        }
    }

}
