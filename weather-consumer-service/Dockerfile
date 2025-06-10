FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

RUN chmod +x gradlew

RUN ./gradlew dependencies

COPY src/ src/

RUN ./gradlew build -x test

EXPOSE 8081
CMD ["java", "-jar", "build/libs/weather-consumer-service-0.0.1-SNAPSHOT.jar"]