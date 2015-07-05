package org.openehealth.tutorial

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.validation.ValidationContext
import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7.validation.builder.RuleBuilder
import org.openehealth.ipf.modules.hl7.validation.builder.ValidationContextBuilder

/**
 * Created by gregorlenz on 05/07/2015.
 */
class SampleRulesBuilder {
    public RuleBuilder forContext(ValidationContext context) {
        new RuleBuilder(context)
                .forVersion('2.2')
                .message('ADT', 'A01').abstractSyntax(
                'MSH',
                'EVN',
                'PID',
                [  {  'NK1'  }  ],
                'PV1',
                [  {  INSURANCE(
                        'IN1',
                        [  'IN2'  ] ,
                        [  'IN3'  ]
                )}]
        )
    }
}
