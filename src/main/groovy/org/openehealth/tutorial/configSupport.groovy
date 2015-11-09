package org.openehealth.tutorial

/**
 * Created by gregorlenz on 01/09/15.
 */

class configSupport {
    final ConfigObject values = new ConfigSlurper().parse(new File("./src/main/resources/config.txt").toURI().toURL())

/*
    final family_name = configFile.common.family_name
*/

    Map getRoutingSlips() {
        return values.Routing_Slips

    }

}