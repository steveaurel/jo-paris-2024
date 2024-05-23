# Stage 1: Build the application using Maven
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Add CA certificates to avoid SSL certificate errors
COPY gitlab.crt /usr/local/share/ca-certificates/gitlab.crt
RUN apt-get update && apt-get install -y ca-certificates
RUN update-ca-certificates

# Run the Maven build
RUN mvn package -DskipTests

# Stage 2: Create the final image using the JAR built in the previous stage
FROM openjdk:17-oracle

# Copy the GitLab certificate
COPY gitlab.crt /usr/local/share/ca-certificates/gitlab.crt
RUN keytool -import -alias gitlab -keystore $JAVA_HOME/lib/security/cacerts -file /usr/local/share/ca-certificates/gitlab.crt -storepass changeit -noprompt

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the volume and entry point
VOLUME /tmp
ENTRYPOINT ["java", "-Djavax.net.debug=ssl", "-jar", "/app.jar"]
