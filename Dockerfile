# Step 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application
FROM openjdk:17-jdk-slim
COPY --from=build /target/loan-management-system-0.0.1-SNAPSHOT.jar app.jar
# Create the data directory for the persistent disk
RUN mkdir -p /opt/render/project/src/data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]