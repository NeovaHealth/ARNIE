package uk.co.neovahealth.ARNIE

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.validation.builder.ValidationRuleBuilder
import org.apache.camel.component.hl7.HL7DataFormat
import org.apache.camel.spring.SpringRouteBuilder

import static org.apache.camel.component.hl7.HL7.messageConforms

class ADTRouting extends SpringRouteBuilder {

    void configure() {

        HapiContext context = new DefaultHapiContext(lookup(SampleRulesBuilder))
        context.getParserConfiguration().setValidating(false)


        ValidationRuleBuilder builder = new ValidationRuleBuilder(){
            @Override
            protected void configure(){
                forVersion(Version.V24)
                .message("ADT", "A01")
                .terser("PID-8", not(empty()));
            }
        };

        context.setValidationRuleBuilder(builder);

        HL7DataFormat hl7 = new HL7DataFormat()
        hl7.setHapiContext(context)


        String hl7listener = "direct:hl7listener"
        String hl7router = "direct:hl7router"

        String inputQueue = "direct:activemq-in"
        String admit = "direct:admit"
        String transfer = "direct:transfer"
        String discharge = "direct:discharge"
        String updateOrCreatePatient = "direct:updateOrCreatePatient"
        String updateVisit = "direct:updateVisit"
        String updateOrCreateVisit = "direct:updateOrCreateVisit"
        String detectHistorical = "direct:detectHistorical"
        String patientRegister = "direct:register"


        String msgLogging = "direct:msgLogging"
        String msgHistory = "msgHistory"

        //entry point
        from(hl7listener)
            .unmarshal(hl7)
            .setHeader("triggerEvent", {it.in.body.getTriggerEvent()})
            .setHeader("visitNameString", {it.in.body.MSH[4].value})
            //.setHeader("data", {inbound -> inbound.in[Message].toString()})
            .transform({it -> it.in.body.generateACK()})
            //.marshal(hl7)
            .to(hl7router)

        from(hl7router)
            .validate(messageConforms())
            .to("routingSlip")
            //.to(msgHistory)

        from(inputQueue)
            //.unmarshal(hl7)
            .bean(ComputeRoutingSlip)

        from(admit).routeId("admit").to(transfer)
        /*
                .choice()
                .when(caller.visitExists()).to("output").endChoice()
                .otherwise()
                    .transform({it -> it})
                    */

        /*rest("/register")
            .get()*/

        from(transfer)
            .transform({it -> it})

        from(discharge)
            .transform({it -> it})

        from(updateOrCreatePatient)
            .transform({it -> it})

        from(updateVisit)
            .transform({it -> it})

        from(msgLogging)
            .unmarshal(hl7)
            .to(msgHistory)

        from(detectHistorical)
            .transform({it -> it})

        from(updateOrCreateVisit)
            .transform({it -> it})

        from(patientRegister)
            .transform({it -> it})

        from("direct:deadLetter")
            .convertBodyTo(String)
            .to('file:target/output')
    }
    
}
