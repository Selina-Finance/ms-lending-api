# Selina Lending API Microservice

The Lending API provides a gateway for making a Quick Quote, Decision In Principle (DIP) and Decision In Principle with Credit Commitment (DIPCC)
applications. 
Our apis enables users to get an idea of how much money can be borrowed against the equity of their home based on their affordability.


## Table of Contents
* [Technologies Used](#technologies-used)
* [Setup](#setup)
* [Usage](#usage)
* [Security](#security)
* [Circuit Breaker](#circuit-breaker)
* [Rate Limiting](#rate-limiting)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Feature requests and roadmap](#feature-requests-and-roadmap)
* [License](#license)


## Technologies Used
- Java 17
- Spring Boot
- OAuth 2
- Resilience4J Circuit Breaker
- Zalando error handling
- TODO Monitoring, alerting, logging, and tracing 


## Setup

The project is built using Gradle and the main application is LendingServiceApplication. 
You can run the application either using docker or run the LendingServiceApplication class via your IDE.

Build and run the project using docker from the root ms-lending-api:

```
docker compose up --build
```

After the application has started you can check the health status:
```
http://localhost:8080/lending/actuator/health
```

TODO  - provide necessary information to DevOps about dynamic configurations for the service in different Cloud environments.

1. When setting up this microservice we will need to create the following vault policies:

* ms-example-jx-staging

```
path "kv/jx-staging" {
capabilities = [ "read", "list"]
}

path "kv/ms-example/jx-staging" {
capabilities = [ "read", "list"]
}
```

TODO

* Then we will need to bind the kubernetes service account to these policies:

```
vault write auth/kubernetes/role/ms-example-jx-staging \
bound_service_account_names=jx-ms-example \
bound_service_account_namespaces=jx-staging \
policies=ms-example-jx-staging \
ttl=8000h

vault write auth/kubernetes/role/ms-example-jx-preview \
bound_service_account_names=preview-preview \
bound_service_account_namespaces="*" \
policies=ms-example-jx-staging \
ttl=8000h

vault write auth/kubernetes/role/ms-example-jx-production \
bound_service_account_names=jx-ms-example \
bound_service_account_namespaces=jx-production \
policies=ms-example-jx-production \
ttl=8000h
```


## Usage
Below are some apis we have for creating, updating and retrieving an application:

Get an Application by application id
```
http://localhost:8080/lending/application/{id}
```

Create DIP Application
```
http://localhost:8080/lending/application/dip
```

Update DIP Application for a given application
```
http://localhost:8080/lending/application/{id}/dip
```

To see the full list of apis, the documentation is available under:
```
http://localhost:8080/lending/swagger-ui/index.html
```

## Security

TODO
- Keycloak
- Middleware authentication

## Circuit Breaker

TODO

## Rate limiting

TODO

## Project Status

Project is: _in progress_ 


## Room for Improvement

- Know issues / bugs

## Feature requests and roadmap
  - Asynchronous requests for creating/updating applications

  - DIP with Credit Commitment applications

  - Quick Quote applications



## License

This project is open-source and available under the [Apache 2.0 License](https://choosealicense.com/licenses/apache-2.0/).

