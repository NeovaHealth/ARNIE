package uk.co.neovahealth.ARNIE

import groovyx.net.http.RESTClient
import org.apache.camel.Exchange
import groovy.json.JsonBuilder

import static groovyx.net.http.ContentType.TEXT

/**
 * Created by gregorlenz on 06/11/15.
 */
class eObsCalls extends ADTProcessing{
    def url = 'http://localhost:8069/'
    def client = new RESTClient(url)
    def loginBody = ["username": "adt", "password": "adt", "database": "nhclinical"]

    def json = new JsonBuilder()
    def databuilder = new JsonBuilder()
    def data = databuilder given_name: 'Arthur', family_name: 'Nudge'
    def total = json data: data, patient_id: '10'


    void login() {
        client.post(path: 'mobile/login', query: loginBody) { resp, data ->
            assert !data.text.contains('invalid')
            /*if (resp.status == 200 && !data.text.contains('invalid')) true
            else false*/
        }
    }

    void patientRegister(Exchange inflight) {
        login()
        data = databuilder given_name: 'Arthur', family_name: 'Nudge'
        json patient_id: getHospitalNumber(inflight), data: data

        client.post(path: 'adt/v1/patient/register', body: json.toString(), requestContentType: TEXT) { resp, data ->
            assert resp.status == 200
            assert data.status == 'success'
        }
    }


    void patientAdmit(Exchange inflight) {
        login()
        return
    }

    void patientTransfer() {
        return
    }

    def patientDischarge(Exchange inflight) {
        json = new JsonBuilder()
        data = databuilder given_name: 'Arthur', family_name: 'Nudge'
        json patient_id: getHospitalNumber(inflight), data: data

        client.post(path: 'adt/v1/patient/discharge', body: json.toString(), requestContentType: TEXT) { resp, data ->
            assert resp.status == 200
            assert data.status == 'success'
            if (resp.status == 200 && data.status == 'success') true
        }
        return false
    }

    void visitUpdate(Exchange inflight) {

    }

    void patientNew() {
        return
    }

    void patientUpdate() {
        return
    }
}
