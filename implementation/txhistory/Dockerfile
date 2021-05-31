FROM maven:3-openjdk-11 AS build
COPY implementation/txhistory/pom.xml /build/
COPY implementation/txhistory/src /build/src/
WORKDIR /build/
RUN mvn clean package

FROM openjdk:11
EXPOSE 8081
ARG JAR_FILE=/build/target/txhistory-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
