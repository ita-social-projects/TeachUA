# =====================================
# STAGE 1: Build Spring Boot Application
# =====================================
FROM maven:3.9.3-amazoncorretto-17 AS build
WORKDIR /app

# Copy necessary files for Maven build
COPY pom.xml checkstyle.xml ./
COPY src ./src

# Build Spring Boot application
RUN mvn clean package

# =================================
# STAGE 2: Final Stage - Run Application
# =================================
FROM tomcat:latest

# Copy the built Spring Boot application from build stage to Tomcat webapps directory
# and change the owner of the copied file to 'tomcat'
COPY --from=build /app/target/dev.war /usr/local/tomcat/webapps/ROOT.war

# Set Spring profile to 'docker' (the same to spring.profiles.active)
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the specified port outside the container
EXPOSE $PORT

# Start Tomcat server
ENTRYPOINT [ "bash", "-c", "./bin/catalina.sh run" ]
