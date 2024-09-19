# TaskManager

A task management application built with Spring Boot and MongoDB, providing a secure, scalable, and RESTful API for managing tasks. The application incorporates JWT-based authentication and role-based authorization.

## Features

- **Task Management**: Create, update, delete, and track tasks.
- **REST API**: Endpoints for interacting with tasks programmatically.
- **MongoDB Integration**: Persistent task storage with MongoDB.
- **Security**: Authentication and authorization via Spring Security and JWT.
- **Input Validation**: Ensures data integrity with Spring Validation.
- **Scalable Architecture**: Suitable for small to large-scale task management.
  
## Technologies Used

- **Spring Boot 3.2.5**
  - Spring Boot Starter Web
  - Spring Boot Starter Data MongoDB
  - Spring Boot Starter Security
  - Spring Boot Starter Validation
- **MongoDB**: NoSQL database for flexible data storage.
- **JWT**: Secure token-based authentication and authorization.
- **Lombok**: Reduces boilerplate code.
- **Docker Compose**: For containerization (optional).

## Prerequisites

- Java 21
- Maven 3.6+
- MongoDB installed and running
- Docker (optional, if using containerization)
