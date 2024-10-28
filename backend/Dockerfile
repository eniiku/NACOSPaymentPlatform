# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS build
WORKDIR /workspace/app

# Copy maven wrapper files
COPY mvnw .
COPY .mvn .mvn

# Copy pom.xml and source code
COPY pom.xml .
COPY src src

# Make the mvnw script executable
RUN chmod +x mvnw

# Build the JAR file
RUN ./mvnw package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]