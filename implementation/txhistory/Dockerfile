FROM maven:3-openjdk-11 AS build
#COPY src ASE_Team_0105/implementation/txhistory/src
#COPY pom.xml ASE_Team_0105/implementation/txhistory/pom.xml
RUN mvn clean package

FROM openjdk:11
EXPOSE 8081
ARG JAR_FILE=target/txhistory-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
