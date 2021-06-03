#FROM maven:3-openjdk-11 AS build
#COPY implementation/txhistory/pom.xml /build/
#COPY implementation/txhistory/src /build/src/
#WORKDIR /build/
#RUN mvn package -B

FROM maven:3-openjdk-11
COPY ./ ./
RUN mvn -f pom.xml package
CMD java -jar target/*.jar

#FROM openjdk:11
#Volume /tmp
#ADD /target/*.jar txhistory-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/txhistory-0.0.1-SNAPSHOT.jar"]
