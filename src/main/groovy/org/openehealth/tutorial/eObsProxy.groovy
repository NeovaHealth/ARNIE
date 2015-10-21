package org.openehealth.tutorial

import groovy.net.xmlrpc.XMLRPCServerProxy as Proxy

/**
 * Created by gregorlenz on 19/10/15.
 */
class eObsProxy extends Proxy{
    eObsProxy(url) { super(url) }

    Object invokeMethod(String methodname, args) {
        super.invokeMethod(methodname, args)
    }
}

