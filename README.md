# Spring Boot MongoDB REST API 

This is a tutorial about learning Spring Data MongoDB to develop a simple Spring Boot REST API.

## Table of Contents

## 1. MongoDB

### 1.1 MongoDB Installation

Can install either from MongoDB website or the Mac terminal.

Website: [Install MongoDB Community Edition](https://www.mongodb.com/docs/manual/administration/install-community/)

Terminal: [How to install Mongodb 5 | latest MAC installation (YouTube)](https://www.youtube.com/watch?v=s1WQ0eEpqqg)

Common problem when installing via Terminal: [zsh: command not found: mongo](https://stackoverflow.com/questions/68695241/zsh-command-not-found-mongo)

### 1.2 Mongosh vs. Studio3T

Can use a shell (Terminal) or GUI to access MongoDB. 

MongoDB Shell (Mongosh) is the quickest way to connect, configure, query, and work with your MongoDB database. It acts as a command-line client of the MongoDB server.

Mongosh: [MongoDB Crash Course](https://www.youtube.com/watch?v=ofme2o29ngU)

Studio3T is an interactive tool for querying, optimizing, and analyzing your MongoDB data. Studio3T also has its own built-in mongo shell, IntelliShell, which offers live error highlighting and smart auto-completion.

Studio3T: [Getting Started with Studio 3T | The GUI for MongoDB](https://www.youtube.com/watch?v=cKHumpkI7c8)

### 1.3 MongoDB Commands

MongoDB has some commonly used commands which have been neatly gathered for quick reference in: [MongoDB-Dark.pdf]()


## 2. REST API

[Spring Data MongoDB Tutorial](https://programmingtechie.com/2021/01/06/spring-data-mongodb-tutorial/)

### 2.1 Dependencies

You can download the starter project with all the needed dependencies at Spring Initializr website with these dependencies:

- Spring Web
- Spring Data MongoDB
- Lombok
- Testcontainers

You can define the MongoDB properties by either using the MongoURI or by defining the host, username, password and database details:

```properties
# Approach 1
spring.data.mongodb.uri=mongodb://localhost:27017/expense-tracker

# Approach 2
spring.data.mongodb.host=localhost
spring.data.mongodb.username=<your-username>
spring.data.mongodb.password=<your-password>
spring.data.mongodb.database=expense-tracker
```

### 2.2 Model

### 2.3 Repository

### 2.4 Service

### 2.5 Controller
