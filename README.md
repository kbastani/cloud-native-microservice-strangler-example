# Microservices: Cloud Native Legacy Strangler Example

This reference application is a Spring Cloud example of implementing a cloud-native [Strangler Pattern](http://www.martinfowler.com/bliki/StranglerApplication.html) using microservices. The project is intended to demonstrate techniques for integrating a microservice architecture with legacy applications in an existing SOA. This reference architecture implements a hybrid cloud architecture that uses best practices for developing _Netflix-like_ microservices using Spring Cloud.

* Cloud Native Microservices
  * Uses best practices for cloud native applications
  * OAuth2 User Authentication
  * Netflix OSS / Spring Cloud Netflix
  * Configuration Server
  * Service Discovery
  * Circuit Breakers
  * API Gateway / Micro-proxy
* Legacy Edge Gateway
  * Legacy application integration layer
  * Adapter for legacy systems to consume microservices
* Lazy Migration of Legacy Data
  * Microservice facades integrate domain data from legacy applications
  * Database records are siphoned away from legacy databases
  * Datasource routing enables legacy systems to use microservices as the system of record
* Strangler Event Architecture
  * Asset capture strategy uses events to guarantee single system of record for resources
  * Durable mirroring of updates back to legacy system

## Architecture Diagram

![Example Cloud Native Strangler Microservice Architecture](http://i.imgur.com/ZhuwpbZ.png)

## Overview

This reference application is based on both common and novel design patterns for building a cloud-native hybrid architecture with both legacy applications and microservices. The reference project includes the following applications.

* Legacy Applications
  * Customer Service
  * Legacy Edge Service
* Microservices
  * Discovery Service
  * Edge Service
  * Config Service
  * User Service
  * Profile Service
  * Profile Web

## Legacy Database Strangulation

When building microservices, the general approach is to take existing monoliths and to decompose their components into microservices. Instead of migrating all legacy applications at once, we can allow an organic process of decomposition to drive the birth of new cloud-native applications that strangle data away from shared databases used by legacy applications. The _cloud-native strangler pattern_ focuses on the complete replacement of a monolith's database access over a period of time.

In this approach microservices will be transitioned to become the system of record for domain data used by strangled legacy applications. The process of performing an on-demand migration of data out of a shared database will require that only one system of record exists at any one time. To solve this, a _Legacy Edge_ application acts as an API gateway to allow legacy applications to talk to new microservices.
 
## Usage

There are two ways to run the reference application, with either Docker Compose or Cloud Foundry, the latter of which can be installed on a development machine using [PCF Dev](https://docs.pivotal.io/pcf-dev/). Since the distributed application is designed to be cloud-native, there is a lot to be gained from understanding how to deploy the example using Cloud Foundry.

### Docker Compose

To run the example using Docker Compose, a `run.sh` script is provided which will orchestrate the startup of each application. Since the example will run 8 applications and multiple backing services, it's necessary to have at least 9GB of memory allocated to Docker.

WARNING: The `run.sh` script is designed to use Docker Machine, so if you're using Docker for Mac, you'll need to modify the `run.sh` script by setting `DOCKER_IP` to `localhost`.

### Cloud Foundry

To run the example using Cloud Foundry, a `deploy.sh` script is provided which will orchestrate the deployment of each application to a simulated cloud-native environment. If you have enough resources available, you can deploy the example on [Pivotal Web Service](http://run.pivotal.io). If you're new to Cloud Foundry, it's highly recommended that you go with the PCF Dev approach, which you can install by following the directions at https://docs.pivotal.io/pcf-dev/.

When you have a CF environment to deploy the example, go ahead and run the `deploy.sh` script in the parent directory of the project. The bash script is commented enough for most to understand the steps of the deployment. Each Cloud Foundry deployment manifest is located in the directory of the application in example project, named `manifest.yml`. The deployment process will deploy the Spring Cloud backing services first, and afterward, each microservice will be deployed one by one until each application is running.

## License

This project is licensed under Apache License 2.0.
