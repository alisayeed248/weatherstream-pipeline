# WeatherStream Pipeline

A production-ready weather data processing pipeline that continuously ingests, transforms, and streams weather data using Spring Boot, Kafka, and PostgreSQL.

## Architecture Overview

The pipeline consists of two main services:

- **Weather Ingestion Service** - Fetches weather data from OpenWeatherMap API and publishes to Kafka
- **Weather Consumer Service** - Consumes weather data from Kafka and stores in PostgreSQL

## Services

### Weather Ingestion Service (Port 8080)
- **Technology**: Spring Boot with Maven
- **Purpose**: Continuously fetch weather data from OpenWeatherMap API
- **Features**:
  - Dynamic scheduling system for multiple cities
  - Configurable fetch intervals per city
  - Kafka producer for streaming data
  - Database tracking of monitored locations

**Key Components**:
- `WeatherService` - Handles API calls and Kafka publishing
- `DynamicSchedulerService` - Manages per-city scheduled tasks
- `TrackedLocation` entity - Stores cities and their fetch intervals

### Weather Consumer Service (Port 8081)
- **Technology**: Spring Boot with Gradle
- **Purpose**: Process and store incoming weather data
- **Features**:
  - Kafka consumer for weather data streams
  - PostgreSQL storage with JPA/Hibernate
  - REST API for querying historical data

**Key Components**:
- `WeatherConsumerService` - Kafka message processing
- `WeatherQueryController` - REST endpoints for data access
- `WeatherRecord` entity - Persistent weather data model

## Infrastructure

### Kafka Topics
- `weather.raw` - Raw weather data from OpenWeatherMap API

### Database Schema
- `tracked_locations` - Cities being monitored with intervals
- `weather_records` - Historical weather data storage

### Docker Services
- **Kafka + Zookeeper** - Message streaming platform
- **PostgreSQL** - Data persistence layer

## Quick Start

### Prerequisites
- Java 17+
- Docker & Docker Compose
- OpenWeatherMap API key

### Setup

1. **Start Infrastructure**
   ```bash
   docker-compose up -d
   ```

2. **Set API Key**
   ```bash
   export OPENWEATHER_API_KEY=your_api_key_here
   ```

3. **Start Services**
   
   Weather Ingestion Service:
   ```bash
   cd weather-ingestion-service
   ./mvnw spring-boot:run
   ```
   
   Weather Consumer Service:
   ```bash
   cd weather-consumer-service
   ./gradlew bootRun
   ```

### Testing the Pipeline

1. **Check Service Health**
   ```bash
   curl http://localhost:8080/hello
   curl http://localhost:8081/api/cities
   ```

2. **Manual Weather Fetch**
   ```bash
   curl "http://localhost:8080/weather?city=London"
   ```

3. **Query Stored Data**
   ```bash
   curl http://localhost:8081/api/weather/London
   ```

## API Endpoints

### Ingestion Service (Port 8080)
- `GET /hello` - Health check
- `GET /weather?city={city}` - Manual weather fetch
- `GET /test-db` - View tracked locations

### Consumer Service (Port 8081)
- `GET /api/cities` - List all monitored cities
- `GET /api/weather/{city}` - Recent weather for city
- `GET /api/weather/{city}/recent?hours=24` - Weather within timeframe

## Configuration

### Environment Variables
- `OPENWEATHER_API_KEY` - Your OpenWeatherMap API key
- Database and Kafka connections configured in `application.properties`

### Database Connection
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/weatherstream
spring.datasource.username=weather
spring.datasource.password=password123
```

### Kafka Configuration
```properties
spring.kafka.bootstrap-servers=localhost:9092
```

## Development Notes

### Current Features
- ✅ Dynamic city scheduling with configurable intervals
- ✅ Kafka-based message streaming
- ✅ PostgreSQL data persistence
- ✅ REST API for data querying
- ✅ Docker-based local development environment

### Planned Enhancements
- [ ] Data processing service for derived metrics (heat index, wind chill)
- [ ] Weather alerts topic and processing
- [ ] AWS deployment with MSK and RDS
- [ ] Comprehensive testing with TestContainers
- [ ] CI/CD pipeline with GitHub Actions

## Project Structure

```
weatherstream-pipeline/
├── weather-ingestion-service/     # Maven-based ingestion service
├── weather-consumer-service/      # Gradle-based consumer service
├── docker-compose.yml            # Local infrastructure
└── README.md                     # This file
```

## Technology Stack

- **Languages**: Java 17
- **Frameworks**: Spring Boot 3.5.0, Spring Kafka, Spring Data JPA
- **Build Tools**: Maven (ingestion), Gradle (consumer)
- **Database**: PostgreSQL 15
- **Messaging**: Apache Kafka
- **Containerization**: Docker
- **External API**: OpenWeatherMap
