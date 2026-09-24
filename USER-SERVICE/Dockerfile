FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
ARG JAR_FILE=target/BACKEND-USERS-SPRING-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_usersbackend.jar
EXPOSE 8080
CMD ["java", "-jar", "app_usersbackend.jar"]