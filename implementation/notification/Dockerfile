FROM maven:3-openjdk-11
EXPOSE 8080 8088 587
COPY ./ ./
RUN mvn -f pom.xml package
CMD java -jar target/*.jar