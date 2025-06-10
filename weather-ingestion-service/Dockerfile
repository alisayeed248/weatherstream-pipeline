FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml first for better layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src/ src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "target/weather-ingestion-service-0.0.1-SNAPSHOT.jar"]