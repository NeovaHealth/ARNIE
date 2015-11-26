package uk.co.neovahealth.ARNIE

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.validation.builder.ValidationRuleBuilder
import org.apache.camel.Predicate
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


        def caller = new eObsCalls()
        def queries = new eObsQueries()
        Predicate isA01 = header('triggerEvent').isEqualTo('A01')

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
            .to(hl7router)
            .transform({it -> it.in.body.generateACK()})
            //.marshal(hl7)

        from(hl7router)
            .validate(messageConforms())
            .to("routingSlip")
            .to(msgHistory)

        from(updateOrCreatePatient).routeId("UpdateOrCreate")
            .choice()
                .when(method(queries, "patientExists")).to("bean:eObsCalls?method=patientUpdate")
                .otherwise().to("bean:eObsCalls?method=patientRegister")

        from(admit).routeId("admit")
            .to("bean:eObsCalls?method=patientAdmit")

        from(transfer)
            .choice()
                .when(method(queries, "visitExists")).to("bean:eObsCalls?method=patientTransfer")
                .otherwise().to("bean:eObsCalls?method=patientRegister").to("bean:eObsCalls?method=patientTransfer")

        from(discharge)
            .choice()
                .when(method(queries, "visitExists")).to("bean:eObsCalls?method=patientDischarge")
                .otherwise().to("bean:eObsCalls?method=patientRegister").to("bean:eObsCalls?method=patientDischarge")

        from(updateVisit)
            .to("bean:eObsCalls?method=visitUpdate")

        from(patientRegister)
            .to("bean:eObsCalls?method=patientRegister")

        from(msgLogging)
            .unmarshal(hl7)
            .to(msgHistory)

        from(detectHistorical)
            .transform({it -> it})

        from(updateOrCreateVisit)
            .transform({it -> it})



    }
    
}
