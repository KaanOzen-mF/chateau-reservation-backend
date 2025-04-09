# Reservation System Backend

This project contains the Spring Boot-based backend API of the Reservation System, designed to be used with a Next.js frontend.

## Description

Provides a RESTful API that allows users to make reservations for specific resources (e.g., rooms, tables, time slots). Includes core functionalities such as user management, resource management, and reservation operations.

## Technologies Used (Tech Stack)

* **Framework:** Spring Boot 3.x
* **Language:** Java 21+
* **API:** Spring Web (REST API)
* **Database Access:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL
* **Security:** Spring Security
* **Utility Libraries:** Lombok, Validation
* **Build Tool:** Maven

## Requirements

* Java Development Kit (JDK) 21 or later
* Apache Maven
* PostgreSQL DBS

## Setup and Running

1.  **Clone the Project:**
    ```bash
    git clone <project-repo-address>
    cd reservation-backend
    ```
    *(Note: This step will be updated later if there is no repo address yet)*

2.  **Database Setup:**
    * Create a database for the project in PostgreSQL (e.g., `reservation_db`).
    * Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` settings in the `src/main/resources/application.properties` file according to your PostgreSQL configuration.

3.  **Compile the Project:**
    ```bash
    mvn clean install
    ```

4.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
    Or run the compiled JAR file:
    ```bash
    java -jar target/reservation-backend-0.0.1-SNAPSHOT.jar
    ```
    *(Note: The JAR file's version number may change)*

The application will start running at `http://localhost:8080` by default.

## Configuration

The main configuration file is `src/main/resources/application.properties`. Database connection and other Spring Boot settings are managed from here.

## API Endpoints

*(This section will be filled in as APIs are developed)*

---

You can add this initial `README.md` content to your project. As you develop, we will update, especially the API endpoints section.

**What to do now?**

Now that we have taken the first step for the documentation, should we return to coding? My suggestion is to **create our first Entity class, `User`** and **add Javadoc comments** for this class. In this way, we both write code and create in-code documentation. Is that ok?
