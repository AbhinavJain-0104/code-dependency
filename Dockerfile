FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target ./target
ENTRYPOINT ["java", "-jar", "target/developer-0.0.1-SNAPSHOT.jar"]