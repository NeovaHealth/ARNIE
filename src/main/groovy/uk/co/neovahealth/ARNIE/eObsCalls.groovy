package uk.co.neovahealth.ARNIE

import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.TEXT


/**
 * Created by gregorlenz on 06/11/15.
 */
class eObsCalls {
    def url = 'http://localhost:8069/'
    def client = new RESTClient(url)
    def origPostBody = '{"data": {"given_name":"John"}, "patient_id": 6}'
    def postBody = ['patient_id': 17, 'data': ['given_name':'John', family_name: 'Test']]
    def loginBody = ["username": "adt", "password": "adt", "database": "nhclinical"]

    def login() {
        client.post(path: 'mobile/login', query: loginBody) { resp, data ->
            assert !data.text.contains('invalid')
            if (resp.status == 200 && !data.text.contains('invalid')) true
            else false
        }
    }

    def patientRegister() {
        client.post(path: 'adt/v1/patient/register', body: origPostBody, requestContentType: TEXT) { resp, data ->
            assert resp.status == 200
            assert data.status == 'error'
            println(data.description)
            if (resp.status == 200) true
        }
    }

    def visitExists() {
        return true
    }

    def patientExists() {
        return true
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
