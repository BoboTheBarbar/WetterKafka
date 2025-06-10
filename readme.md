# WetterKafka

A Spring Boot application for weather data processing using Kafka messaging.

## Prerequisites

- Java 21
- Gradle (wrapper included)
- Kafka (for local development)
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

### 2. Build the Project

```bash
./gradlew build
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on the default Spring Boot port (8080).

## Development

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
- `src/main/resources/` - Configuration files
- `src/test/` - Test files and resources
- `build.gradle.kts` - Gradle build configuration

## Technologies Used

- Spring Boot 3.5.0
- Spring Kafka
- Java 21
- Testcontainers (for testing)
- OpenWeather API integration

## API Endpoints

The application provides weather-related endpoints. Check the HTTP request files in `src/test/resources/` for example usage.