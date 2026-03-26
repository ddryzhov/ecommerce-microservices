# E-Commerce Microservices

Production-ready microservices application built with Spring Boot and Spring Cloud.

## Architecture

| Service            | Port | Description              |
|--------------------|------|--------------------------|
| order-service      | 8081 | Order management         |
| inventory-service  | 8082 | Inventory management     |

## Tech Stack

- Java 26 · Spring Boot 4.0.3 · Spring Cloud 2025.1.0
- MySQL · Liquibase · MapStruct · Lombok
- Apache Kafka
- Docker · GitHub Actions · AWS ECS Fargate

## Getting Started
```bash
mvn clean install
```
