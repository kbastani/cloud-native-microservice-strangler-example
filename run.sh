#!/usr/bin/env bash

set -e

# Build the project and docker images
# mvn clean install

# Export the active docker machine IP
export DOCKER_IP=$(docker-machine ip $(docker-machine active))

# Remove existing containers
docker-compose stop
docker-compose rm -f

# Start database
docker-compose up -d mysql

# Start the config + discovery service first and wait for it to become available
java -jar ./microservices/config-service/target/*-SNAPSHOT.jar & java -jar ./microservices/discovery-service/target/*-SNAPSHOT.jar &

java -jar ./microservices/user-service/target/*-SNAPSHOT.jar &
java -jar ./microservices/edge-service/target/*-SNAPSHOT.jar &
java -jar ./microservices/profile-service/target/*-SNAPSHOT.jar &
java -jar ./legacy-applications/customer-service/target/*-SNAPSHOT.jar &
java -jar ./legacy-applications/legacy-edge/target/*-SNAPSHOT.jar &