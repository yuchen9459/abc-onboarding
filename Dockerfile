FROM openjdk:17-jdk-slim

ENV LOADER_PATH="/home/app/conf"

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
