FROM maven:3-openjdk-11
EXPOSE 8091 8080
COPY ./ ./
RUN mvn -f pom.xml package
CMD java -jar target/*.jar