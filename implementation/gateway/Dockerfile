FROM maven:3-openjdk-11
COPY ./ ./
RUN mvn -f pom.xml package
CMD java -jar target/gateway-0.0.1-SNAPSHOT.jar