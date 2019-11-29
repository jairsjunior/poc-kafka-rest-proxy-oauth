# POC Kafka Rest-Proxy Using SASL OAuth Mechanism

This POC use the implementation of `RestResourceExtension` interface to capture authorizathion header and
create an virtual Jaas configuration file for each context of an user authenticated using the IMS authorization 
service. Using this virtual jaas file we can propagate the user token to the kafka broker.

## Getting Started

At the folder [docker](./docker) we had an Readme file with all instructions to run the test ambient.
