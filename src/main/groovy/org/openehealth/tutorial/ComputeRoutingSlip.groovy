package org.openehealth.tutorial;

import org.apache.camel.RoutingSlip;

/**
 * Created by gregorlenz on 10/09/15.
 */
public class ComputeRoutingSlip {

    @RoutingSlip
    //TODO Exchange??



    @RoutingSlip
    String nextSteps(int body){
        switch (body){
            case 1: return "direct:admit, direct:updatePatient";
            case 2: return "direct:transfer, direct:updatePatient";
            case 3: return "direct:discharge";
            default: return "direct:updatePatient";
        }


    }
}
