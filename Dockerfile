FROM openjdk:jre-alpine

COPY target/kafka_challenge_3a-0.0.1-SNAPSHOT.jar kafka_challenge_3a-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/kafka_challenge_3a-0.0.1-SNAPSHOT.jar"]
