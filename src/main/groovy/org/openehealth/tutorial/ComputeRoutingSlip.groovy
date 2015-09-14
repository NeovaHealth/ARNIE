package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v22.message.ADT_A01
import ca.uhn.hl7v2.model.v22.message.ADT_AXX
import ca.uhn.hl7v2.model.v22.segment.MSH
import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.RoutingSlip
import sun.security.pkcs.EncodingException;

/**
 * Created by gregorlenz on 10/09/15.
 */
public class ComputeRoutingSlip {


    @RoutingSlip
    String messageSteps(Message inflightmsg) {
        HapiContext hapictx = new DefaultHapiContext()

        Parser p = hapictx.getGenericParser()

        Message hapimsg

        try{
            hapimsg = p.parse(inflightmsg)
        } catch (EncodingException e) {
            e.printStackTrace()
        }

        ADT_AXX adt_axx = (ADT_AXX) hapimsg;

        ADT_A01 adt_a01 = (ADT_A01) inflightmsg;

        try{
            MSH msh = adt_axx.getMSH()
        }

        catch (HL7Exception e) {
            e.printStackTrace()
            return
        }
        return "direct:admit, direct:updatePatient"
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
