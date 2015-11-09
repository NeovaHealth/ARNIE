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

        return configParser.getRoutingSlips().get(msgTrigger)
    }

}