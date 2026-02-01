# Stage 1: Build the JAR using a modern Maven/Java image
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the JAR using a modern, stable JRE image
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/loan-management-system-0.0.1-SNAPSHOT.jar app.jar

# Create a folder for the H2 database
RUN mkdir -p /data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]