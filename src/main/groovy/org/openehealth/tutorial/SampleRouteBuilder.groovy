package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.validation.builder.ValidationRuleBuilder
import org.apache.camel.Exchange
import org.apache.camel.component.hl7.HL7DataFormat
import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.tutorial.SampleRulesBuilder

import static org.apache.camel.component.hl7.HL7.messageConforms


class SampleRouteBuilder extends SpringRouteBuilder {

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

        from('file:target/input')
                .convertBodyTo(String)
                .to('direct:input')

        from('direct:input')
                .unmarshal(hl7)
                .validate(messageConforms())
                .transmogrify { msg ->
                    msg.PID[7][1] = msg.PID[7][1].value.substring(0, 8)
                    msg.PID[8] = msg.PID[8].mapGender()
                    msg.PV1[3][2] = ''
                    msg.PV1[3][3] = ''
                    //msg.PV1[8] = ''
                    msg
                }

                .setHeader(Exchange.FILE_NAME) { exchange ->
                    exchange.in.body.MSH[4].value + '.hl7'
                }

                .convertBodyTo(String)
                .to('file:target/output')



        from('direct:input1').transmogrify { it * 2 }
        from('direct:input2')
                .unmarshal(hl7)
                .reverse()
                .setHeader(Exchange.FILE_NAME) { exchange ->
                    'file.reverse'
                }
                .to('file:target/output')


    }
    
}
