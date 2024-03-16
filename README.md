# Spring Boot MongoDB REST API

This is a guide about learning Spring Data MongoDB to develop a simple CRUD Expense Manager REST API.

## Getting Started

### Prerequisites

- Git
- Java 11
- Maven

### Running the App

1.  Open your terminal or command prompt.

2.  Clone the repository using Git:

    ```bash
    git clone https://github.com/arsy786/spring-boot-mongodb-rest-api.git
    ```

3.  Navigate to the cloned repository's root directory:

    ```bash
    cd spring-boot-mongodb-rest-api
    ```

4.  Run the following Maven command to build and start the service:

    ```bash
    # For Maven
    mvn spring-boot:run

    # For Maven Wrapper
    ./mvnw spring-boot:run
    ```

The application should now be running on `localhost:8080`.

### Database Configuration

[2.1 Dependencies](#21-dependencies)

## Table of Contents

[1. MongoDB](#1-mongodb)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[1.1 MongoDB Installation](#11-mongodb-installation)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[1.2 Mongosh vs. Studio3T](#12-mongosh-vs-studio3t)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[1.3 MongoDB Commands](#13-mongodb-commands)
<br>
[2. REST API](#2-rest-api)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[2.1 Dependencies](#21-dependencies)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[2.2 Model](#22-model)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[2.3 Repository](#23-repository)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[2.4 Creating REST API (Service & Controller)](#24-creating-rest-api-service--controller)

## 1. MongoDB

### 1.1 MongoDB Installation

Can install from MongoDB website or the Mac terminal.

Website: [Install MongoDB Community Edition](https://www.mongodb.com/docs/manual/administration/install-community/)
<br>
Terminal: [How to install Mongodb 5 | latest MAC installation (YouTube/HiteshChoudhary)](https://www.youtube.com/watch?v=s1WQ0eEpqqg)
<br>
Common problem when installing via Terminal: [zsh: command not found: mongo](https://stackoverflow.com/questions/68695241/zsh-command-not-found-mongo)

### 1.2 Mongosh vs. Studio3T

Can use a shell (Terminal) or GUI to access MongoDB.

MongoDB Shell (Mongosh) is the quickest way to connect, configure, query, and work with your MongoDB database. It acts as a command-line client of the MongoDB server.

Mongosh: [MongoDB Crash Course (YouTube/WebDevSimplified)](https://www.youtube.com/watch?v=ofme2o29ngU)

Studio3T is an interactive tool for querying, optimizing, and analyzing your MongoDB data. Studio3T also has its own built-in mongo shell, IntelliShell, which offers live error highlighting and smart auto-completion.

Studio3T: [Getting Started with Studio 3T | The GUI for MongoDB (YouTube/Studio3T)](https://www.youtube.com/watch?v=cKHumpkI7c8)

### 1.3 MongoDB Commands

MongoDB has some commonly used commands which have been neatly gathered for quick reference in: [MongoDB-Dark.pdf](https://github.com/arsy786/spring-boot-mongodb-rest-api/blob/master/MongoDB-Dark.pdf)

## 2. REST API

[Spring Data MongoDB Tutorial (ProgrammingTechie)](https://programmingtechie.com/2021/01/06/spring-data-mongodb-tutorial/)

### 2.1 Dependencies

You can download the starter project with all the needed dependencies at Spring Initializr website with these dependencies:

- Spring Web
- Spring Data MongoDB
- Lombok
- Testcontainers

You can define the MongoDB properties by either using the MongoURI or by defining the host, username, password and database details:

`application.properties`:

```properties
# Approach 1
spring.data.mongodb.uri=mongodb://localhost:27017/expense-tracker

# Approach 2
spring.data.mongodb.host=localhost
spring.data.mongodb.username=<your-username>
spring.data.mongodb.password=<your-password>
spring.data.mongodb.database=expense-tracker
```

NOTE: If authentication enabled, username and password must be provided.

### 2.2 Model

`expense.java`:

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("expense")
public class Expense {

    @Id
    private String id;

    @Field("name")
    @Indexed(unique = true)
    private String expenseName;

    @Field("category")
    private ExpenseCategory expenseCategory;

    @Field("amount")
    private BigDecimal expenseAmount;

}
```

`ExpenseCategory.java`:

```java
public enum ExpenseCategory {
    ENTERTAINMENT, GROCERIES, RESTAURANT, UTILITIES, MISC
}
```

- Normal POJO class with annotations.
- Lombok annotations reduce boilerplate code.
- To define a Model Class as a MongoDB Document, we are going to use the @Document(“expense”) where expense is the name of the Document.
- @Id represents a unique identifier for our Document.
- We can represent different fields inside the Document using the @Field annotation.
- By default, Spring Data creates the field inside the document using the fieldName of the model class (Eg: expenseName), but we can override this by providing the required value to the annotation eg: @Field(“name”).
- To be able to easily retrieve the documents, we can also create an index using the @Indexed annotaion.
- We can also specify the unique=true property to make sure that this field is unique.

### 2.3 Repository

Spring Data MongoDB provides an interface called MongoRepository which provides an API to perform read and write operations to MongoDB.

`ExpenseRepository.java`:

```java
@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    @Query("{'name': ?0}")
    Optional<Expense> findByName(String name);
}
```

We can also perform custom queries using the @Query annotation and by passing in the required query we need to run to this annotation.
Spring Data will inject the value of the name field into the query, in the place of the ?0 placeholder.

### 2.4 Creating REST API (Service & Controller)

Need to add the business logic in the Service layer.

`ExpenseService.java`:

```java
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public void addExpense(Expense expense) {
        expenseRepository.insert(expense);
    }

    public void updateExpense(Expense expense) {
        Expense savedExpense = expenseRepository.findById(expense.getId()).orElseThrow(
                () -> new RuntimeException(String.format("Cannot Find Expense by ID %s", expense.getId())));

        savedExpense.setExpenseName(expense.getExpenseName());
        savedExpense.setExpenseCategory(expense.getExpenseCategory());
        savedExpense.setExpenseAmount(expense.getExpenseAmount());

        expenseRepository.save(expense);
    }

    public Expense getExpense(String name) {
        return expenseRepository.findByName(name).orElseThrow(
                () -> new RuntimeException(String.format("Cannot Find Expense by Name - %s", name)));
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }
}
```

Need to add the API Endpoints in the Controller layer.

`ExpenseController.java`:

```java
@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/")
    public ResponseEntity addExpense(@RequestBody Expense expense) {
        expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity updateExpense(@RequestBody Expense expense) {
        expenseService.updateExpense(expense);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/{name}")
    public ResponseEntity getExpenseByName(@PathVariable String name) {
        return ResponseEntity.ok(expenseService.getExpense(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
```
