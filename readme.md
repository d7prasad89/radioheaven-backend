## RadioHeaven Backend Application 🚀🚀

A Spring Boot backend application for managing and streaming songs and sermons, with AWS S3 integration for file storage.

## Features

- RESTful API for managing songs and sermons
- MySQL database integration using Spring Data JPA
- AWS S3 integration for file download and streaming
- Docker-ready microservice architecture

## Technologies

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- AWS SDK for Java (S3)
- Maven

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL
- AWS account with S3 access

### Configuration

Edit `src/main/resources/application.properties` to set your MySQL credentials.

AWS credentials are picked up from the default provider chain (environment variables, `~/.aws/credentials`, etc.).

### Build and Run

```bash
mvn clean install
mvn spring-boot:run