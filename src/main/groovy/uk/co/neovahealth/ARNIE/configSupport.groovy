package uk.co.neovahealth.ARNIE

/**
 * Created by gregorlenz on 01/09/15.
 */

class configSupport {
    final ConfigObject values = new ConfigSlurper().parse(new File("./src/main/resources/config.txt").toURI().toURL())

    Map getRoutingSlips() {
        return values.Routing_Slips

    }

}