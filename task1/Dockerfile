FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL authors="Grigorii Tishchuk"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "-cp", "target/task1-1.0-SNAPSHOT.jar", "TemperatureConverter"]