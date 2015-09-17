package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.validation.builder.ValidationRuleBuilder
import org.apache.camel.Exchange
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
                forVersion(Version.V22)
                .message("ADT", "A01")
                .terser("PID-8", not(empty()));
            }
        };

        context.setValidationRuleBuilder(builder);


        HL7DataFormat hl7 = new HL7DataFormat()
        hl7.setHapiContext(context)

        String hl7listener = "direct:hl7listener"
        String inputQueue = "direct:activemq-in"
        String admit = "direct:admit"
        String transfer = "direct:transfer"
        String discharge = "direct:discharge"
        String updatePatient = "direct:updatePatient"
        String updateVisit = "direct:updateVisit"

        String hl7router = "direct:hl7router"

        from(hl7listener)
            //.to(inputQueue)
            .to(hl7router)

        from(hl7router)
            .unmarshal(hl7)
            .validate(messageConforms())
            //.setHeader(hl7)
            .bean(ComputeRoutingSlip)


        from(inputQueue)
            //.unmarshal(hl7)
            //.setHeader("myRoute").method(ComputeRoutingSlip)
            //.routingSlip("myRoute")
            .bean(ComputeRoutingSlip)

        from(admit)
            .transform({it -> it})
            .to(msgHistory)

        from(transfer)
            .transform({it -> it})

        from(discharge)
            .transform({it -> it})

        from(updatePatient)
            .transform({it -> it})

        from(updateVisit)
            .transform({it -> it})


        from('direct:input')
                .unmarshal(hl7)
                //.onException(Exception.Class)
                //    .handled(true)
                 //   .transform(ack())
                 //   .end()
                .validate(messageConforms())
                .transmogrify { msg ->
                    msg.PID[7][1] = msg.PID[7][1].value.substring(0, 8)
                    msg.PID[8] = msg.PID[8].mapGender()
                    msg.PV1[3][2] = ''
                    msg.PV1[3][3] = ''
                    //msg.PV1[8] = ''
                    msg
                }
                //.transform(ack())
                .setHeader(Exchange.FILE_NAME) { exchange ->
                    exchange.in.body.MSH[4].value + '.hl7'
                }
                .convertBodyTo(String)
                .to('file:target/output')



        from('direct:inputMultiply').transmogrify { it * 2 }
        from('direct:inputReverse')
                .unmarshal(hl7)
                .reverse()
                .setHeader(Exchange.FILE_NAME) { exchange ->
                    'file.reverse'
                }
                .to('file:target/output')


    }
    
}
