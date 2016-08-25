# Microservices

Here you will find each microservice for the cloud native application. The microservices are split into two categories: platform services and domain services. The difference is that the platform services are offered on a PaaS, while the domain services consume platform services. Domain services implement business logic while platform services support cross-cutting concerns.

* Microservices - Platform services
  * Config service - Spring Cloud Config
  * Discovery service - Netflix Eureka
  * Edge service - Netflix Zuul
  * User service - Spring Cloud OAuth2
* Microservices - Domain services
  * Profile service - User profile information