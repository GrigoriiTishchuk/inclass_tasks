# Stage 1: Build Fat-JAR with Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime image with JavaFX & GUI support
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Зависимости Linux GUI для X11 (Xming)
RUN apt-get update && apt-get install -y \
    libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0 mesa-utils wget unzip \
    && rm -rf /var/lib/apt/lists/*

# Загрузка SDK JavaFX 21
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

COPY --from=build /app/target/task1-1.0-SNAPSHOT.jar app.jar
COPY .env.docker .env
ENV DISPLAY=host.docker.internal:0.0

CMD ["java", "--module-path", "/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml", "-Dprism.order=sw", "-jar", "app.jar"]