package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v22.message.ADT_A01
import ca.uhn.hl7v2.model.v22.message.ADT_AXX
import ca.uhn.hl7v2.model.v22.segment.MSH
import ca.uhn.hl7v2.parser.Parser
import org.apache.camel.Header;
import org.apache.camel.RoutingSlip
import org.apache.camel.component.hl7.HL7DataFormat
import sun.security.pkcs.EncodingException;

/**
 * Created by gregorlenz on 10/09/15.
 */
public class ComputeRoutingSlip {


    @RoutingSlip
    String messageSteps(Message inflightmsg, @Header("ContextHeader") HL7DataFormat contex) {
        HapiContext hapictx = new DefaultHapiContext()

        Parser p = hapictx.getGenericParser()

        Message hapimsg

        try{
            hapimsg = p.parse(inflightmsg.toString())
        } catch (EncodingException e) {
            e.printStackTrace()
        }

        String version = hapimsg.getVersion()

        MSH msh = hapimsg.getMSH()

        String msgType = msh.getMessageType().getMessageType().getValue()
        String msgTrigger = msh.getMessageType().getTriggerEvent().getValue()

        for (int i=0; i<100; i++){
            System.out.println(msgType + " " + msgTrigger)
        }

        switch (msgTrigger){
            case 'A01': return "direct:admit";
            case 'A02': return "direct:transfer";
            case 'A03': return "direct:discharge";
            default: return
        }
    }

    /*
    @RoutingSlip
    String nextSteps(int body) {
        switch (body) {
            case 1: return "direct:admit, direct:updatePatient";
            case 2: return "direct:transfer, direct:updatePatient";
            case 3: return "direct:discharge";
            default: return "direct:updatePatient";
        }
    }
    */
}
