# Use a base image of Java 21
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/Microservicio7-1.0.0.jar /app/microservicio7.jar

# Define the port on which the application will run
EXPOSE 8086

# Command to run the application with the docker profile
ENTRYPOINT ["java", "-jar", "/app/microservicio7.jar", "--spring.profiles.active=docker"]