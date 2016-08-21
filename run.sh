#!/usr/bin/env bash

set -e

# Build the project and docker images
mvn clean install

# Export the active docker machine IP
export DOCKER_IP=$(docker-machine ip $(docker-machine active))

# Remove existing containers
docker-compose stop
docker-compose rm -f

# Start database
docker-compose up -d mysql

# Start the config + discovery service first and wait for it to become available
java -jar ./config-service/target/*-SNAPSHOT.jar & java -jar ./discovery-service/target/*-SNAPSHOT.jar &

java -jar ./user-service/target/*-SNAPSHOT.jar &
java -jar ./edge-service/target/*-SNAPSHOT.jar &
java -jar ./profile-service/target/*-SNAPSHOT.jar &
java -jar ./customer-legacy/target/*-SNAPSHOT.jar &