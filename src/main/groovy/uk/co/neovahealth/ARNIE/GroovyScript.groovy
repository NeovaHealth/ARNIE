package uk.co.neovahealth.ARNIE

import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import groovy.json.JsonBuilder

/**
 * Created by gregorlenz on 08/12/15.
 */
import groovy.net.xmlrpc.*

def loginBody = ["username": "adt", "password": "adt", "database": "nhclinical"]

def client = new XMLRPCServerProxy('http://localhost:8069/xmlrpc/2/', true)
def response = client.login(loginBody)


