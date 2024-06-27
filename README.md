# TaskApp

TaskApp is a Spring Boot application that demonstrates CRUD operations for managing tasks, along with basic authentication using BasicAuth with roles assigned to users.

## Features

- **CRUD Operations**: Perform Create, Read, Update, and Delete operations for tasks.
- **Basic Authentication**: Secure endpoints using BasicAuth with roles assigned to users.
- **Role-Based Access Control**: Restrict access to certain endpoints based on user roles (User and Admin).

## Setup Instructions

### Prerequisites

- Java 21
- Docker Desktop
- Git

1. **Clone the repository**:

   ```
   git clone https://github.com/IvaMatesic/taskApp.git
   cd taskApp
   ```

2. **Run the application using Gradle**:

   ```
   ./gradlew bootRun
   ```

3. **Run all tests with Gradle**:
    ```
    ./gradlew test
    ```

4. **Test manually using Postman or similar tool**:

    **Endpoints**  
  
    Tasks API
    ```
    Create Task: POST /private/tasks (Admin only)
    Update Task: PUT /private/tasks/{id} (Admin only)
    Get All Tasks: GET /private/tasks (Admin/User)
    Get Task by ID: GET /private/tasks/{id} (Admin/User)
    Delete Task: DELETE /private/tasks/{id} (Admin only)
    ```

    Hello API
    ```
    Say Hello (Public): GET /public/hello
    Say Hello (Private): GET /private/hello (Authenticated users)
    ```

    Expected body for POST/PUT requests
   ```
   {
       "title": "Sample task",
       "summary": "Sample summary of the task",
       "dueDate": "2024-07-01"
   }
   ```


    **Available Users (Basic Auth)**
     
      **Admin**
    
      - username: 'admin'
      - password: 'adminsecret'

   
      **Regular user**
     
      - username: 'regularUser'
      - password: 'usersecret'
