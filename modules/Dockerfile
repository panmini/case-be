# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /data

# Spring Boot default
EXPOSE 8080 8080
# Remote Debug
EXPOSE 2919 2919

# Create required folders for logging
RUN mkdir -p /data/logs && chmod 777 /data/logs

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:2919"

# Copy the built JAR file using the version passed from Maven
COPY target/thy-0.1.1.jar thy.jar

ENTRYPOINT ["java", "-jar", "thy.jar"]