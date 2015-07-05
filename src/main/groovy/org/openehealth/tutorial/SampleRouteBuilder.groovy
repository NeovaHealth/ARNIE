package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import org.apache.camel.spring.SpringRouteBuilder

class SampleRouteBuilder extends SpringRouteBuilder {

    void configure() {

        HapiContext context = new DefaultHapiContext(org.openehealth.tutorial.SampleRulesBuilder)

        from('direct:input1').transmogrify { it * 2 }
        from('direct:input2').reverse()
    }
    
}
