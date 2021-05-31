FROM maven:3-openjdk-11 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f pom.xml clean package

FROM openjdk:11
EXPOSE 8082
ARG JAR_FILE=target/shipment-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
