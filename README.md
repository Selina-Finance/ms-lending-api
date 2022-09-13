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
- Logback and Slf4j
- Spring Cloud Sleuth


## Setup

The project is built using Gradle and the main application is LendingServiceApplication. 
You can run the LendingServiceApplication class via your IDE.

Docker:

```
TODO
```

After the application has started you can check the health status:
```
http://localhost:8080/actuator/health
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

Get an Application by external application id
```
http://localhost:8080/application/{id}
```

Create DIP Application
```
http://localhost:8080/application/dip
```

Update DIP Application for a given application by external application id
```
http://localhost:8080/application/{id}/dip
```

To see the full list of apis, the documentation is available under:
```
http://localhost:8080/swagger-ui/index.html
```

## Security
To access our API endpoints you will need a security token. \
Using your credentials you can execute the following request to get your token:
```
curl --location --request POST 'http://localhost:8080/auth/token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "clientId": "your-client-id",
    "clientSecret": "your-client-secret"
}'
```
In the response you will get an access token and expiration period of its validity. \
Then, to make your request authenticated please add the next header to your request using the token you've just got.
```
'Authorization: Bearer your-token-value'
```
## Circuit Breaker

We use [Resilience4j](https://resilience4j.readme.io/docs) as a part of our fault tolerance solution.
Our default configuration means that when something goes wrong in a chain of request execution - we push a circuit breaker into the OPEN state for 1 minute and answer HTTP 502 Bad Gateway to all requests.

After 1 minute period, we change the circuit breaker state to HALF_OPEN and start gradually evaluating the current condition of the system. If the system is already healthy - we push the circuit breaker into the CLOSED state and continue to serve our clients in a usual way.

In total, if you got HTTP 502 Bad Gateway as a response, it means that your request is valid but we cannot serve it right now. Please, retry in 1 minute.

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
