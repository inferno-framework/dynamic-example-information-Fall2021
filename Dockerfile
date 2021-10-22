FROM openjdk:11
ADD target/fhirApi-0.0.1-SNAPSHOT.jar api.jar
ENTRYPOINT ["java", "-jar","api.jar"]
EXPOSE 8080