# Virtual Pet App

## Overview
This is a Spring Boot application that allows users to manage pets for a virtual pet app. Users can sign up, log in, and perform CRUD operations on their pets. Admin users can access all pets in the system.

## Features
- User Authentication (Signup/Login)
- JWT-based Authentication
- CRUD operations for pets
- Role-based access control (Admin role for managing all pets)

## Technologies Used
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Lombok
- MongoDB (for database operations)

## API Endpoints

### Authentication Controller (`/auth`)
- **POST `/login`**: Log in a user and return a JWT token.
- **POST `/signup`**: Create a new user account.

### Pet Controller (`/pets`)
- **GET `/pets/test`**: A test endpoint to verify the server is running.
- **POST `/pets`**: Create a new pet (requires authentication).
- **GET `/pets/myPets`**: Retrieve all pets for the authenticated user.
- **PUT `/pets/{id}`**: Update an existing pet (requires authentication).
- **DELETE `/pets/{id}`**: Delete a pet (requires authentication).

## Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
2. Ensure you have Java 11 or higher and Maven installed.
3. Build the project:
   ```bash
   mvn clean install
4. Run the application
   ```bash
   mvn spring-boot:run
## Configuration
- Update the `application.properties` file with your MongoDB connection details.
- Ensure CORS is set up for your frontend application (default is `http://localhost:3000`).

## Running Tests
You can run tests using:
   ```bash
   mvn test
   ```
## Logging
This application uses SLF4J for logging, along with Lombok's `@Slf4j` annotation for easy integration of logging functionality across classes. 

Logging levels such as `INFO`, `WARN`, and `ERROR` are used to provide feedback during the application's operation, which helps in debugging and monitoring.

- **INFO**: Indicates general information about application events, such as successful user logins and signups.
- **WARN**: Indicates potential issues, such as invalid password attempts or user not found scenarios.
- **ERROR**: Indicates errors that occur during processing, such as authentication failures or issues with database interactions.

Make sure to configure your logging framework (like Logback or Log4j) in the `src/main/resources` directory to control logging output and formatting.

## License
This project is licensed under the MIT License.
