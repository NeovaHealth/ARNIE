package org.openehealth.tutorial

/**
 * Created by gregorlenz on 31/08/15.
 */

import groovy.util.ConfigSlurper

class configSupport {

    final ConfigObject configFile = new ConfigSlurper().parse(new File("/config.txt").toURI().toURL())


    println configFile.autoAck


}
