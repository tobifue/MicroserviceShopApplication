FROM maven:3-openjdk-11 AS build
COPY implementation/txhistory/pom.xml /build/
COPY implementation/txhistory/src /build/src/
WORKDIR /build/
RUN mvn clean package

maven-jar-plugin:3.2.0:jar
FROM openjdk:11
EXPOSE 8081
ARG JAR_FILE=implementation/txhistory/maven-jar-plugin:3.2.0:jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
