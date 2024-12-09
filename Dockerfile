# Stage 1: Build the application
FROM openjdk:17-jdk AS build
WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY src src

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Ensure Maven wrapper is executable
RUN chmod +x mvnw

# Build the application without running tests
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image using OpenJDK 17
FROM openjdk:17-jdk
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
