# Stage 1: Build the application using Maven
FROM maven:latest AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Run the Maven build
RUN mvn clean package -DskipTests

# Stage 2: Create the final image using the JAR built in the previous stage
FROM openjdk:17-oracle

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the volume and entry point
VOLUME /tmp
ENTRYPOINT ["java", "-Djavax.net.debug=ssl", "-jar", "/app.jar"]
