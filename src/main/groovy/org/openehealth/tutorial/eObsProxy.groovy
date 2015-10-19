package org.openehealth.tutorial

import java.lang.reflect.Proxy

/**
 * Created by gregorlenz on 19/10/15.
 */
class eObsProxy extends Proxy{
    eObsProxy(url) { super(url) }

    Object invokeMethod(String methodname, args) {
        super.invokeMethod('nh.' + methodname, args)
    }
}

def eObs = new eObsProxy('http://localhost:8069')

eObs.login('adt', 'adt') { loginToken ->
    def activities = get_activities(loginToken)
}