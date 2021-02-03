FROM openjdk:8u181-alpine3.8

WORKDIR /

COPY target/uberjar/rest-demo-0.1.0-SNAPSHOT-standalone.jar rest-demo.jar
EXPOSE 3000

CMD java -jar rest-demo.jar