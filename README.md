# WeatherStream Pipeline

A production-ready weather data processing pipeline that continuously ingests, transforms, and streams weather data using Spring Boot, Kafka, and PostgreSQL.

## Quick Start

### Prerequisites
- Linux environment (Ubuntu 20.04+ or WSL)
- OpenWeatherMap API key ([get one free here](https://openweathermap.org/api))

### Installation

1. **Install dependencies:**
   ```bash
   # Update package list
   sudo apt update
   
   # Install Git
   sudo apt install git
   
   # Install Docker and Docker Compose
   sudo apt install docker.io docker-compose-v2
   
   # Add your user to docker group (avoid needing sudo)
   sudo usermod -aG docker $USER
   
   # Restart shell session for group changes
   newgrp docker
   ```

2. **Clone and setup:**
   ```bash
   git clone https://github.com/your-username/weatherstream-pipeline
   cd weatherstream-pipeline
   ```

3. **Set your API key:**
   ```bash
   export OPENWEATHER_API_KEY=your_api_key_here
   ```

4. **Start the pipeline:**
   ```bash
   ./weathercli start
   ```

### Usage

```bash
# Add cities to track
./weathercli add-city london 5
./weathercli add-city tokyo 10

# List tracked cities  
./weathercli list-cities

# Remove a city
./weathercli remove-city london

# Stop all services
./weathercli stop
```

### Verification

After starting the pipeline, verify it's working:

```bash
# Check service health
curl http://localhost:8080/hello
curl http://localhost:8081/api/cities

# View weather data for a city (after a few minutes)
curl http://localhost:8081/api/weather/london
```

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

## CLI Commands

The `weathercli` script provides an easy interface to manage the pipeline:

| Command | Description |
|---------|-------------|
| `./weathercli start` | Start all services |
| `./weathercli stop` | Stop all services |
| `./weathercli list-cities` | Show all tracked cities |
| `./weathercli add-city <name> <interval>` | Add city with interval in minutes |
| `./weathercli remove-city <name>` | Remove city from tracking |

## API Endpoints

### Ingestion Service (Port 8080)
- `GET /hello` - Health check
- `GET /weather?city={city}` - Manual weather fetch
- `GET /test-db` - View tracked locations
- `POST /api/cities` - Add new city to track
- `DELETE /api/cities/{cityName}` - Remove city from tracking

### Consumer Service (Port 8081)
- `GET /api/cities` - List all monitored cities
- `GET /api/weather/{city}` - Recent weather for city (default: 10 records)
- `GET /api/weather/{city}/recent?hours=24` - Weather within timeframe

## Configuration

### Environment Variables
- `OPENWEATHER_API_KEY` - Your OpenWeatherMap API key (required)
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

## Troubleshooting

### Common Issues

**API Key Error:**
```
❌ Error: OpenWeatherMap API key not set
```
**Solution:** Set your API key with `export OPENWEATHER_API_KEY=your_key_here`

**Docker Permission Error:**
```
permission denied while trying to connect to the Docker daemon socket
```
**Solution:** Run `newgrp docker` or restart your terminal session

**Services Not Starting:**
Check Docker Compose logs:
```bash
docker compose logs
docker compose ps
```

**No Weather Data:**
Verify the data flow:
```bash
# Check ingestion logs
docker compose logs weather-ingestion

# Check consumer logs  
docker compose logs weather-consumer

# Check database
docker compose exec postgres psql -U weather -d weatherstream -c "SELECT COUNT(*) FROM weather_records;"
```

## Development Features

### Current Features
- ✅ Dynamic city scheduling with configurable intervals
- ✅ Kafka-based message streaming
- ✅ PostgreSQL data persistence
- ✅ REST API for data querying
- ✅ Docker-based local development environment
- ✅ CLI interface for easy management

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
├── weathercli                    # CLI management script
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

## Getting Help

If you encounter issues:
1. Check the [Troubleshooting](#troubleshooting) section
2. View service logs with `docker compose logs [service-name]`
3. Verify your API key is set correctly
4. Ensure Docker is running and your user has permissions