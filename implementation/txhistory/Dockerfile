FROM maven:3-openjdk-11 AS build
COPY src /implementation/txhistory/src
COPY pom.xml /implementation/txhistory/pom.xml
RUN mvn -f implementation/txhistory/pom.xml clean package

FROM openjdk:11
EXPOSE 8081
ARG JAR_FILE=target/txhistory-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
