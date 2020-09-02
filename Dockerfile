FROM openjdk:14-buster

COPY producer-api-0.0.1.jar .

CMD java -jar producer-api-0.0.1.jar
