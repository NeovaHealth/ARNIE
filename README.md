# ARNIE

ARNIE Resilient NeovaHealth Integration Engine 

Master branch: [![Build Status](https://travis-ci.org/NeovaHealth/ARNIE.svg?branch=master)](https://travis-ci.org/NeovaHealth/ARNIE)

![alt tag](https://github.com/NeovaHealth/ARNIE/blob/master/ARNIE%20overview.png)

ARNIE is an Integration Engine that parses HL7 messages of multiple versions and transmits the information to another interface. 
At the time this is the REST interface of Neova Health's electronic observation system 'eObs'.

ARNIE builds upon the [Open Health Integration Platform](https://github.com/oehf/ipf), leveraging its DSL to create a routing engine which is easily understandable and at the same time tremendously powerful.
An Apache ActiveMQ module ensures that messages regarding the same patient are processed in separate threads, thus guaranteeing a consistent order of messages.
Apache Camel then routes messages through different stations using a [Routing Slip](http://www.enterpriseintegrationpatterns.com/patterns/messaging/RoutingTable.html) depending on the message type that had been received.
All messages are stored in a PostgreSQL endpoint, separated in tables of successfully processed messages and messages that failed due to an error.

Contributions more than welcome!

Feel free to contact the maintainer for questions: Gregor Lenz <gregorlenz@neovahealth.co.uk>