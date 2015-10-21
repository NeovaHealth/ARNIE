package org.openehealth.tutorial
import groovy.net.xmlrpc.XMLRPCServerProxy as Proxy

/**
 * Created by gregorlenz on 20/10/15.
 */

def remote = new Proxy('http://localhost:8069/')

//def logintoken = remote.login('adt', 'adt')
def activities = remote.get_activities()

//println(activities)
/*

def remote = new eObsProxy('http://issues.apache.org/jira/rpc/xmlrpc')

remote.login('user', 'user') { loginToken ->
    def projects = getProjectsNoSchemes(loginToken)
    println "${projects.size()} projects found"
}
*/

