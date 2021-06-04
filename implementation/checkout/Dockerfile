FROM maven:3-openjdk-11
COPY ./ ./
RUN mvn -f pom.xml package
CMD sleep 150s && java -jar target/checkout-2.4.4.jar