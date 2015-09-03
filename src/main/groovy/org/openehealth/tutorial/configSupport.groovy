package org.openehealth.tutorial

/**
 * Created by gregorlenz on 01/09/15.
 */


final ConfigObject configFile = new ConfigSlurper().parse(new File("/home/gregorlenz/Development/ipf-hl7/src/main/resources/config.txt").toURI().toURL())

println(configFile)
