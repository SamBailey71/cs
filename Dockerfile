FROM openjdk:17-oracle
COPY target/clarityExercise-0.0.1-SNAPSHOT.jar clarityExercise-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/clarityExercise-0.0.1-SNAPSHOT.jar"]

