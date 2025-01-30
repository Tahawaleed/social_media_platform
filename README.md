
# Social Media Platform API

## Overview

This is a Spring Boot application that serves as a social media platform API. It supports user registration, login, post creation, commenting, liking posts, and following other users. The API also supports searching posts and users based on keywords.

---

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17 or higher**
- **MySQL Database (or any other compatible database)**
- **Maven 3.x**
- **Postman** (Optional, for testing APIs)

---

## Setting Up the Project

### 1. Clone the Repository

Clone the project repository to your local machine:

```bash
git clone https://github.com/Tahawaleed/social_media_platform.git
```

### 2. Database Setup

- Ensure that MySQL is installed and running on your machine.
- Create a database named `social_media_platform`.

```sql
CREATE DATABASE social_media_platform;
```

- Update the `application.properties` with your MySQL credentials (if different from the default ones):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/social_media_platform
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

### 3. Build the Project

Navigate to the project directory and build the project using Maven:

```bash
cd social_media_platform
mvn clean install
```

### 4. Run the Application

Once the build is successful, you can run the application using Maven:

```bash
mvn spring-boot:run
```

This will start the application on `http://localhost:8080/social-media-platform`.

---


## Resources

The project includes several resources for setting up and testing the application:

### 1. **Database**
The `social_media_platform.sql` file contains the SQL schema for the database. You can use this to set up the database tables for the application.

**Path**: `resources/database/social_media_platform.sql`

### 2. **Postman Collection**
The Postman collection and environment files are provided for testing the APIs. Import these files into Postman to test all available API endpoints.

- **Environment File**: `Social Media Platform Env.postman_environment.json`
- **Collection File**: `Social Media Platform.postman_collection.json`

**Path**: `resources/postman/`

### 3. **MySQL Workbench**
The `social_media_platform.mwb` file is the MySQL Workbench file for visualizing the database schema. Open this file in MySQL Workbench to view the database structure.

**Path**: `resources/workbench/social_media_platform.mwb`

---

## API Documentation

### Base URL

```
http://localhost:8080/social-media-platform
```

The API follows REST principles, and endpoints are described below.

### Authentication

The API uses JWT tokens for user authentication.

- **Register User**: POST `/auth/register`
- **Login User**: POST `/auth/login`

### Endpoints

#### 1. **User Registration** 

**POST** `/auth/register`

- **Request Body**: 
  ```json
  {
      "username": "exampleUser",
      "email": "user@example.com",
      "password": "password",
      "profilePicture": "image_url",
      "bio": "Short bio"
  }
  ```
- **Response**:
  ```json
  {
      "message": "User registered successfully!"
  }
  ```

#### 2. **User Login**

**POST** `/auth/login`

- **Request Body**:
  ```json
  {
      "email": "user@example.com",
      "password": "password"
  }
  ```

- **Response**:
  ```json
  {
      "token": "your-jwt-token",
      "user": {
          "id": 1,
          "username": "exampleUser",
          "email": "user@example.com",
          "profilePicture": "image_url",
          "bio": "Short bio"
      }
  }
  ```

#### 3. **Get User Details by ID**

**GET** `/users/{id}`

- **Request**:
  ```bash
  GET http://localhost:8080/social-media-platform/users/1
  ```

- **Response**:
  ```json
  {
      "id": 1,
      "username": "exampleUser",
      "email": "user@example.com",
      "profilePicture": "image_url",
      "bio": "Short bio"
  }
  ```

#### 4. **Create Post**

**POST** `/posts`

- **Request Body**:
  ```json
  {
      "title": "My First Post",
      "content": "This is the content of the post"
  }
  ```

- **Response**:
  ```json
  {
      "id": 1,
      "title": "My First Post",
      "content": "This is the content of the post",
      "user": {
          "id": 1,
          "username": "exampleUser"
      },
      "createdAt": "2025-01-31T12:34:56"
  }
  ```

#### 5. **Update Post**

**PUT** `/posts/{id}`

- **Request Body**:
  ```json
  {
      "title": "Updated Post Title",
      "content": "Updated content of the post"
  }
  ```

- **Response**:
  ```json
  {
      "id": 1,
      "title": "Updated Post Title",
      "content": "Updated content of the post",
      "user": {
          "id": 1,
          "username": "exampleUser"
      },
      "createdAt": "2025-01-31T12:34:56"
  }
  ```

#### 6. **Delete Post**

**DELETE** `/posts/{id}`

- **Request**:
  ```bash
  DELETE http://localhost:8080/social-media-platform/posts/1
  ```

- **Response**:
  ```json
  {
      "message": "Post deleted successfully"
  }
  ```

#### 7. **Add Comment to Post**

**POST** `/posts/{id}/comments`

- **Request Body**:
  ```json
  {
      "commentStr": "This is a comment on the post"
  }
  ```

- **Response**:
  ```json
  {
      "id": 1,
      "title": "Post Title",
      "content": "Post content",
      "comments": [
          {
              "id": 1,
              "user": "commentingUser",
              "content": "This is a comment on the post"
          }
      ]
  }
  ```

#### 8. **Like Post**

**POST** `/posts/{id}/like`

- **Response**:
  ```json
  {
      "message": "Post liked successfully"
  }
  ```

#### 9. **Follow User**

**POST** `/users/{id}/follow`

- **Response**:
  ```json
  {
      "message": "User followed successfully"
  }
  ```

---

## Swagger Documentation

You can explore the API further using Swagger UI:

- Access Swagger UI at: `http://localhost:8080/social-media-platform/swagger-ui/index.html`

---

## Caching

The application uses caching to optimize performance on certain endpoints (e.g., fetching posts, users, and followers).

- Cache eviction occurs when a post is created, updated, or deleted.
- Cacheable endpoints are marked with `@Cacheable` annotations.

---

## Troubleshooting

- Ensure that your MySQL database is running and accessible.
- Check the application logs for any errors or issues during startup or execution.

---
