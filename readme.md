# WetterKafka

A Spring Boot application for weather data processing using Kafka messaging.

## Prerequisites

- Java 21
- Gradle (wrapper included)
- Docker and Docker Compose (for running mongodb and kafka)
- OpenWeather API key

## Setup

### 1. Configure API Key

The preferred way is to configure the OpenWeather API key in your local configuration file:

1. Get a free API key from [OpenWeatherMap](https://openweathermap.org/api)
2. Update `src/main/resources/application-local.yaml` with your API key:

```yaml
openweather:
  api-key: "your_actual_api_key_here"
```

Alternatively, you can set the environment variable:
```bash
export OPENWEATHER_API_KEY=your_actual_api_key_here
```

### 2. Start Infrastructure Services

The project uses Docker Compose to run MongoDB and other services. Start the required services:

```bash
cd docker
docker-compose up -d
```

This will start:
- **MongoDB 8.0** on port `27017`
  - Username: `admin`
  - Password: `password`
  - Database: `wetter`

To check if services are running:
```bash
docker-compose ps
```

To stop services:
```bash
docker-compose down
```

To stop services and remove volumes (deletes data):
```bash
docker-compose down -v
```

### 3. Build the Project

```bash
./gradlew build
```

### 4. Run the Application

```bash
./gradlew bootRun
```

The application will start on the default Spring Boot port (8080).

## Development

### MongoDB Configuration

See `application.yaml`

### Running Tests

```bash
./gradlew test
```

The project uses Testcontainers for integration testing, which will automatically start required services (Kafka) during test execution.

### Local Profile

For local development, you can use the local profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Project Structure

- `src/main/java/de/training/wetterkafka/` - Main application source code
  - `gateway/mongodb/` - MongoDB data access layer
  - `gateway/openweatherclient/` - OpenWeather API client
  - `gateway/wettercontroller/` - REST controllers
- `src/main/resources/` - Configuration files
- `src/test/` - Test files and resources
- `docker/` - Docker Compose configuration
  - `docker-compose.yaml` - Infrastructure services
  - `mongodb/` - MongoDB initialization scripts
- `build.gradle.kts` - Gradle build configuration

## Technologies Used

- Spring Boot 3.5.0
- Spring Kafka
- Spring Data MongoDB
- MongoDB 8.0
- Java 21
- Docker & Docker Compose
- Testcontainers (for testing)
- OpenWeather API integration

## API Endpoints

The application provides weather-related endpoints. Check the HTTP request files in `src/test/resources/` for example usage.