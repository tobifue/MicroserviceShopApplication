FROM maven:3-openjdk-11
EXPOSE 8080 8086
COPY ./ ./
RUN mvn -f pom.xml package
CMD java -jar target/checkout-2.4.4.jar