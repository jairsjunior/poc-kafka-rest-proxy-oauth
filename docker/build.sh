#!/bin/sh

rm rest-proxy/libs/kafka*
cp ../target/kafka-rest-oauth-1.0-SNAPSHOT-jar-with-dependencies.jar ./rest-proxy/libs

docker-compose -f docker-compose-slim-restproxy.yml build --no-cache