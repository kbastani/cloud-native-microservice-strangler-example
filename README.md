# Microservices: Cloud Native Legacy Strangler Example

This reference application is a Spring Cloud example of implementing a cloud-native [Strangler Pattern](http://www.martinfowler.com/bliki/StranglerApplication.html) using microservices. The project is intended to demonstrate techniques for integrating a microservice architecture with legacy applications in an existing SOA. This reference architecture implements a hybrid cloud architecture that uses best practices for developing _Netflix-like_ microservices using Spring Cloud.

* Cloud Native Microservices
  * Uses best practices for cloud native applications
  * OAuth2 User Authentication
  * Netflix OSS / Spring Cloud Netflix
  * Configuration Server
  * Service Discovery
  * Circuit Breakers
  * Distributed Tracing
  * API Gateway / Micro-proxy
* Legacy Edge Gateway
  * Legacy application integration layer
  * Adapter for legacy systems to consume microservices
* Lazy Migration of Legacy Data
  * Microservice facades integrate domain data from legacy applications
  * Database records are siphoned away from legacy databases
  * Datasource routing enables legacy systems to use microservices as the system of record
* Strangler Event Architecture
  * Event sourcing is used to capture assets from domain resources in legacy systems
  * Asset capture strategy uses events to guarantee single system of record for resources
  * Writes are locked during the migration of data from the legacy system to microservices

## Architecture Diagram

![Example Cloud Native Strangler Microservice Architecture](http://i.imgur.com/4xgMaM7.jpg)

## Online Store Domain

This reference application is based on both common and novel design patterns for building a cloud-native hybrid architecture with both legacy applications and microservices. The reference project includes the following applications.

* Legacy Applications
  * Customer Service
  * Legacy Edge Service
* Microservices
  * Discovery Service
  * Edge Service
  * User Service
  * Account Service
  * Web/Edge Application

## Legacy Database Strangulation

When building microservices, the general approach is to take existing monoliths and to decompose their components into microservices. Instead of migrating all legacy applications at once, we can allow an organic process of decomposition to drive the birth of new cloud-native applications that strangle data away from shared databases used by legacy applications. The _cloud-native strangler pattern_ focuses on the complete replacement of a monolith's database access over a period of time.

In this approach microservices will be transitioned to become the system of record for domain data used by strangled legacy applications. The process of performing an on-demand migration of data out of a shared database will require that only one system of record exists at any one time. To solve this, a _Legacy Edge_ application acts as an API gateway to allow legacy applications to talk to new microservices, switching between database and HTTP for data access.

To strangle data away from a shared database, legacy applications need to be able to switch between data access protocols—either a database connection or HTTP—to retrieve domain data. When domain data is migrated to a microservice, the monolith will need to stop reading that data from the shared database—and instead access it from REST APIs exposed by microservices. 

This reference architecture is a proof of concept for how to migrate to a cloud-native application architecture while building microservices without having to take a _lift and shift_ approach for migrating legacy applications and databases into the cloud. 

## License

This project is licensed under Apache License 2.0.
