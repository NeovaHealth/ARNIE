package uk.co.neovahealth.ARNIE


import ca.uhn.hl7v2.validation.builder.support.NoValidationBuilder
import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7.validation.builder.RuleBuilder

/**
 * Created by gregorlenz on 05/07/2015.
 */

class SampleRulesBuilder extends NoValidationBuilder{

    public RuleBuilder forContext(DefaultValidationContext context) {
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
