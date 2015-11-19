package uk.co.neovahealth.ARNIE

import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import org.apache.camel.Exchange

import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.ANY
import static groovyx.net.http.ContentType.HTML



/**
 * Created by gregorlenz on 06/11/15.
 */
class eObsCalls {
    def url = 'http://localhost:8069/'
    def client = new RESTClient(url)
    def origPostBody = '{"data": {"given_name":"John"}, "patient_id": 7}'
    def postBody = ['patient_id': 17, 'data': ['given_name':'John', family_name: 'Test']]
    def loginBody = ["username": "adt", "password": "adt", "database": "nhclinical"]
    def hosp_number, patient_id, given_name, family_name, dob
    def payload = new JsonSlurper().parse(new File('payload.json'))
    def stringJSON = (String) payload

    def login() {
        client.post(path: 'mobile/login', query: loginBody) { resp, data ->
            assert !data.text.contains('invalid')
            if (resp.status == 200 && !data.text.contains('invalid')) true
            else false
        }
    }

    def patientRegister(Exchange inflight) {
        hosp_number = inflight.in.body.PID[3][1].value
        client.post(path: 'adt/v1/patient/register', body: (String) payload, requestContentType: TEXT) { resp, data ->
            assert resp.status == 200
            assert data.status == 'success'
            println(data.description)
            if (resp.status == 200) true
        }
    }



    def patientAdmit() {
        return true
    }

    def patientTransfer() {
        return true
    }

    def patientDischarge() {
        return true
    }

    def patientNew() {
        return true
    }

    def patientUpdate() {
        return true
    }
}
