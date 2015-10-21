package org.openehealth.tutorial

/**
 * Created by gregorlenz on 20/10/15.
 */

def eObs = new eObsProxy('http://localhost:8069')


eObs.login('adt', 'adt') { loginToken ->
    def activities = get_activities(loginToken)

}