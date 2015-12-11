package uk.co.neovahealth.ARNIE

import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import org.apache.camel.Exchange
import groovy.json.JsonBuilder
import groovy.net.http.*
import sun.nio.fs.UnixUserPrincipals

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

import static groovyx.net.http.ContentType.TEXT

/**
 * Created by gregorlenz on 06/11/15.
 */
class eObsCalls extends ADTProcessing{
    def http = new HTTPBuilder( 'http://localhost:8069/web')

    def http2 = new JsonRpcHttpClient(new URL('http://localhost:8069/web'))

    //def user = http2.invoke("login", new Object[] {"username", "password", "database"}, UnixUserPrincipals.User.class);

/*    def url = 'http://localhost:8069/'
    def client = new RESTClient(url)
    def loginBody = ["username": "adt", "password": "adt", "database": "nhclinical"]

    def json = new JsonBuilder()
    def databuilder = new JsonBuilder()
    def data = databuilder given_name: 'Arthur', family_name: 'Nudge'
    def total = json data: data, patient_id: '10'


    void login() {
        client.post(path: 'mobile/login', query: loginBody) { resp, data ->
            assert !data.text.contains('invalid')
            *//*if (resp.status == 200 && !data.text.contains('invalid')) true
            else false*//*
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
        json = new JsonBuilder()

        return
    }

    void patientTransfer() {
        return
    }

    void patientDischarge(Exchange inflight) {
        login()
        json = new JsonBuilder()
        data = databuilder given_name: 'Arthur', family_name: 'Nudge'
        json patient_id: getHospitalNumber(inflight), data: data

        client.post(path: 'adt/v1/patient/discharge', body: json.toString(), requestContentType: TEXT) { resp, data ->
            assert resp.status == 200
            assert data.status == 'success'
        }
    }

    void visitUpdate(Exchange inflight) {

    }

    void patientNew() {
        return
    }

    void patientUpdate() {
        return
    }*/
}
