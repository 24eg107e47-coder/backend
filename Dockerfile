# Stage 1: Build stage using Maven and Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the Spring Boot JAR (skipping unit tests)
RUN mvn clean package -DskipTests

# Stage 2: Lightweight runtime stage with Java 21 JRE
FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Render injects PORT dynamically; fallback to 8080
ENV PORT=8080
EXPOSE 8080

# Execute Spring Boot application
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar /app/app.jar"]
