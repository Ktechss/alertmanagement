# Create the README.md content with the updated information
readme_content = """
# Alert Management System

A Java-based alert management application that integrates with PostgreSQL for database storage and supports secure JWT-based authentication.

---

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java** (17 or higher)
- **Maven** (3.8+)
- **Docker** (20.10+)
- **Docker Compose** (v1.29+)

---

## Setup Instructions

### **1. Clone the Repository**

```bash
git clone https://github.com/Ktechss/alertmanagement.git
cd alertmanagement
```
### **2. Build the Application:**

Run the following command to compile and package the application, skipping tests:

```bash
mvn clean install -DskipTests
```

This will generate the JAR file for the application.

### **3. Run with docker:**

Use Docker Compose to build and start the application and PostgreSQL database:

```bash
docker compose up --build
```
This command will:

- Start a PostgreSQL database instance on port 55004.
- Build and run the Java application on port 55005.

### **3. API Testing:**

Once the application is running, you can test its APIs using tools like Postman or cURL. The application will be available at http://localhost:55005. Below are some endpoints to test:

Authentication

- Method: POST

- Endpoint: /api/auth

- Request Body (JSON):
```JSON
{
  "username": "testuser",
  "password": "test123"
}
```
- Response (JSON):
```JSON
{
  "token": "<JWT Token>"
}
```

Secure Endpoint

- Method: GET
- Endpoint: /api/notify
- Authorization Header: Add the JWT token from the /api/auth response.
```
Authorization: Bearer <JWT Token>
```
Environment Variables
The application uses environment variables defined in the docker-compose.yml file. You can customize them as needed:

Database Config:
- DB_HOST: Hostname of the database (default: db)
- DB_PORT: Port of the database (default: 5432)
- DB_NAME: Name of the database (default: alert_management)
- DB_USER: Database username (default: test)
- DB_PASSWORD: Database password (default: test)
  
JWT Config:
- JWT_SECRET: Secret key for JWT signing
- JWT_EXPIRATION: Token expiration time in milliseconds
  
Mail Config:
- MAIL_HOST: Mail server hostname (default: smtp.gmail.com)
- MAIL_PORT: Mail server port (default: 587)
- MAIL_USERNAME: Email address for the mail server
- MAIL_PASSWORD: Password for the email address
  
Application Ports
- Database Port: 55004
- Application Port: 55005
- Stopping the Application

To stop the containers, use:
```bash
docker compose down
```
