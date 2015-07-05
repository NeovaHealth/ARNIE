package org.openehealth.tutorial

import org.apache.camel.model.ProcessorDefinition

class SampleModelExtensionModule {
      
     static ProcessorDefinition reverse(ProcessorDefinition self) {
         self.transmogrify { it.reverse() } 
     }
     
}
