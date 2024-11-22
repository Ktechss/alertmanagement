# Base image with JDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/alertmanagement-0.0.1-SNAPSHOT.jar app.jar

# Copy the .env file
COPY .env /app/.env

# Expose application port
EXPOSE 8080


# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
