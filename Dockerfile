# =================================
# STAGE 1: Build React Application
# =================================
FROM node:16.20.0 AS react-build
WORKDIR /react-app

# Clone repository and checkout to specific branch
RUN git clone https://github.com/ita-social-projects/teachuaclient.git .
RUN git checkout develop

# Install dependencies and build application
RUN npm install --legacy-peer-deps
RUN npm run build

# =====================================
# STAGE 2: Build Spring Boot Application
# =====================================
FROM maven:3.9.3-amazoncorretto-17 AS build
WORKDIR /app

# Copy necessary files for Maven build
COPY pom.xml .
COPY checkstyle.xml .
COPY src ./src

# Create necessary directories
RUN mkdir -p src/main/resources/frontend

# Copy built React application
COPY --from=react-build /react-app/build /app/src/main/resources/frontend

# Build Spring Boot application
RUN mvn clean package

# =================================
# STAGE 3: Final Stage - Run Application
# =================================
FROM tomcat:latest

# Copy the built Spring Boot application from build stage to Tomcat webapps directory
# and change the owner of the copied file to 'tomcat'
COPY --from=build /app/target/dev.war /usr/local/tomcat/webapps/dev.war

# Set Spring profile to 'dev'
ENV SPRING_PROFILES_ACTIVE=dev

# Expose the specified port outside the container
EXPOSE $PORT

# Start Tomcat server
ENTRYPOINT [ "bash", "-c", "./bin/catalina.sh run" ]
