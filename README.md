# Chateau Reservation Backend

This is the backend API service for the Chateau Reservation system, built with Spring Boot. It provides RESTful endpoints for user registration, authentication (JWT-based), and potentially other reservation-related functionalities.

## 🚀 Technologies

* **Framework**: [Spring Boot](https://spring.io/projects/spring-boot) v3.4.4
* **Language**: [Java](https://www.java.com/) 21
* **Web**: Spring Web MVC
* **Data Persistence**: Spring Data JPA
* **Database**: [PostgreSQL](https://www.postgresql.org/)
* **Security**: Spring Security, JSON Web Tokens (JWT) using `jjwt` library
* **Validation**: Spring Boot Starter Validation (used in `User` entity and DTOs)
* **Build Tool**: [Maven](https://maven.apache.org/)
* **Utilities**: [Lombok](https://projectlombok.org/)

## 📋 Prerequisites

Before running the application locally, ensure you have the following installed:

* JDK 21 or later
* Apache Maven
* A running PostgreSQL database instance

## ⚙️ Setup and Running

1.  **Clone the Repository:**
    ```bash
    git clone <backend-repo-url> # Replace with your repository URL
    cd reservation-backend
    ```

2.  **Database Setup:**
    * Ensure your PostgreSQL server is running.
    * Create a database for the application (the default name used in properties is `reservation_db`).
    * Ensure you have a PostgreSQL user/role with privileges on this database (the default user in properties is `postgres`). You might need to create one or use the default `postgres` user.
    * _(Optional: Example psql commands, adjust user/db name if needed)_
        ```sql
        -- Example psql commands (Run as PostgreSQL superuser):
        -- CREATE DATABASE reservation_db;
        -- CREATE USER your_db_user WITH PASSWORD 'your_db_password'; -- Or use the default 'postgres' user
        -- GRANT ALL PRIVILEGES ON DATABASE reservation_db TO your_db_user; -- Grant privileges if you created a new user
        ```

3.  **Configure Application:**
    * Navigate to the `src/main/resources/` directory.
    * Create a file named `application.properties` (you can copy `application.properties.example` if you create one).
    * Edit `application.properties` with your specific database credentials and a **secure** JWT secret key.

    **Example `application.properties` Structure:**
    ```properties
    # src/main/resources/application.properties

    spring.application.name=reservation-backend

    # Database Configuration (Update with your actual settings)
    spring.datasource.url=jdbc:postgresql://localhost:5432/reservation_db
    spring.datasource.username=postgres
    spring.datasource.password=YOUR_SECURE_DATABASE_PASSWORD # <-- IMPORTANT: Use your actual password here, DO NOT COMMIT this file with real passwords!

    # JPA Configuration
    spring.jpa.hibernate.ddl-auto=update # Creates/updates schema automatically based on entities. Use 'validate' or 'none' in production.
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.show-sql=true # Logs executed SQL statements (useful for dev, disable in prod)
    spring.jpa.properties.hibernate.format_sql=true # Formats the logged SQL

    # ===============================================
    # JWT SECURITY SETTINGS
    # ===============================================
    # Base64 encoded secret key (Generate a strong random key!)
    # IMPORTANT: Use a strong, long, randomly generated secret key.
    # Store this securely in production (e.g., environment variable, secrets manager).
    application.security.jwt.secret-key=YOUR_SECURE_BASE64_ENCODED_JWT_SECRET # <-- IMPORTANT: Replace with your actual generated key, DO NOT COMMIT real secrets!

    # Token expiration time in milliseconds (e.g., 3600000 ms = 1 hour)
    application.security.jwt.expiration=3600000

    # Security Debug Logging (Useful for development)
    logging.level.org.springframework.security=DEBUG

    # Server Configuration
    server.port=8080 # Default port
    ```
    * **⚠️ Security Warning**: Never commit your actual database password or JWT secret key directly into your Git repository. Use environment variables or a proper secrets management solution for production environments. Consider adding `application.properties` to your `.gitignore` file and using an `application.properties.example` file with placeholders in the repository.

4.  **Build and Run:**
    * **Option 1 (Using Maven Wrapper - Recommended):**
        ```bash
        ./mvnw spring-boot:run
        ```
    * **Option 2 (Using global Maven):**
        ```bash
        mvn spring-boot:run
        ```
    * The application will start, typically on `http://localhost:8080`. Check the console output for confirmation and any potential errors.

## 🌐 API Endpoints

The following API endpoints are currently available:

* **Authentication:**
    * `POST /api/auth/login`: Authenticates a user with email and password, returns a JWT token upon success. Requires a `LoginRequest` body (`{"email": "...", "password": "..."}`).
* **User Management:**
    * `POST /api/user/register`: Registers a new user. Requires a `User` object in the request body matching the entity structure (excluding fields like `id`, `createdAt`, `updatedAt` which are auto-generated/managed).
* **Testing:**
    * `GET /api/test/hello`: A test endpoint that requires a valid JWT. Returns a greeting including the authenticated user's email if successful.

_(Future: Add link to Swagger UI or OpenAPI documentation here once implemented)_

## 📁 Project Structure (Overview)

The project follows a standard layered architecture within the base package `com.reservationsystem.reservation_backend`:

* `config`: Contains Spring configuration classes (e.g., `SecurityConfig`, `WebConfig` for CORS).
* `controller`: Spring MVC controllers defining REST API endpoints and handling HTTP requests (e.g., `AuthController`, `UserController`, `TestController`).
* `dto`: Data Transfer Objects used specifically for API request/response bodies (e.g., `AuthResponse`, `LoginRequest`).
* `entity`: JPA entity classes mapped to database tables (e.g., `User`).
* `exception`: Custom exception classes (e.g., `ResourceAlreadyExistsException`) and potentially global exception handlers.
* `repo`: Spring Data JPA repository interfaces for database access (e.g., `UserRepo`).
* `security`: Components related to Spring Security and JWT (e.g., `JwtAuthenticationFilter`).
* `service`: Business logic layer containing services used by controllers (e.g., `UserService`, `UserDetailsServiceImpl`, `JwtService`).

## 🔗 Frontend Connection

This backend API serves the frontend application located at:

* **Frontend Repository:** `https://github.com/KaanOzen-mF/chateau-reservation-frontend` 

---

_This document should be updated as the project evolves._