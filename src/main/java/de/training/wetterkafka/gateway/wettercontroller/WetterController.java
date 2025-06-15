package de.training.wetterkafka.gateway.wettercontroller;

import com.fasterxml.jackson.databind.JsonNode;
import de.training.wetterkafka.application.GeoLocationService;
import de.training.wetterkafka.domain.GeoLocationDomain;
import de.training.wetterkafka.gateway.openweatherclient.GeoLocationOpenWeatherAdapter;
import de.training.wetterkafka.gateway.openweatherclient.GeoLocationOpenWeatherDTO;
import de.training.wetterkafka.gateway.openweatherclient.OpenWeatherProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Arrays;

@RestController
public class WetterController {
    private final GeoLocationService geoLocationService;
    private final RestClient restClient;
    private final OpenWeatherProperties properties;

    private static final Logger logger = LogManager.getLogger(WetterController.class);

    public WetterController(GeoLocationService geoLocationService, 
                           RestClient restClient, 
                           OpenWeatherProperties properties) {
        this.geoLocationService = geoLocationService;
        this.restClient = restClient;
        this.properties = properties;
    }

    @GetMapping("/weather/{cityName}")
    public ResponseEntity<JsonNode> weather(@PathVariable String cityName) {
        GeoLocationOpenWeatherDTO firstGeolocation = getGeolocationByCityName(cityName);

        JsonNode jsonNode = restClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path(properties.getWeatherByGeolocationEndpoint())
                                .queryParam("lat", firstGeolocation.lat())
                                .queryParam("lon", firstGeolocation.lon())
                                .queryParam("appid", properties.getApiKey())
                                .build())
                .retrieve()
                .body(JsonNode.class);

        return ResponseEntity.ok(jsonNode);
    }

    private GeoLocationOpenWeatherDTO getGeolocationByCityName(String cityName) {
        // Zuerst in der Datenbank suchen
        GeoLocationDomain location = geoLocationService.findByName(cityName);

        if (location != null) {
            // Domain zu MongoDB DTO konvertieren
            logger.info("Found location in database: {}", cityName);
            return GeoLocationOpenWeatherAdapter.GEO_LOCATION_OPEN_WEATHER_ADAPTER.toOpenWeatherDto(location);
        }

        // Wenn nicht in DB gefunden, von OpenWeather API holen
        logger.info("Location not found in database, fetching from OpenWeather API: {}", cityName);
        
        GeoLocationOpenWeatherDTO[] locations = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getGeolocationByCityEndpoint())
                        .queryParam("q", cityName)
                        .queryParam("limit", 5)
                        .queryParam("appid", properties.getApiKey())
                        .build())
                .retrieve()
                .body(GeoLocationOpenWeatherDTO[].class);

        if (locations == null || locations.length == 0) {
            throw new IllegalArgumentException("No locations found for city: " + cityName);
        }

        GeoLocationOpenWeatherDTO firstLocation = Arrays.asList(locations).getFirst();
        
        // API-Ergebnis in Datenbank speichern für zukünftige Anfragen
        saveLocationToDatabase(firstLocation);
        
        String formatted = "Retrieved %d geolocations from API".formatted(locations.length);
        logger.info(formatted);

        return firstLocation;
    }

    /**
     * Speichert eine Location von der API in der Datenbank
     */
    private void saveLocationToDatabase(GeoLocationOpenWeatherDTO mongoDto) {
        try {
            // OpenWeather DTO zu Domain konvertieren
            GeoLocationDomain domain = GeoLocationOpenWeatherAdapter.GEO_LOCATION_OPEN_WEATHER_ADAPTER.toDomain(mongoDto);
            
            // Validierung
            if (GeoLocationOpenWeatherAdapter.GEO_LOCATION_OPEN_WEATHER_ADAPTER.isValidDomain(domain)) {
                // Normalisierung anwenden
                GeoLocationDomain normalizedDomain = GeoLocationOpenWeatherAdapter.GEO_LOCATION_OPEN_WEATHER_ADAPTER.normalize(domain);
                
                // In Datenbank speichern
                geoLocationService.save(normalizedDomain);
                logger.info("Saved location to database: {}", normalizedDomain.name());
            } else {
                logger.warn("Invalid domain data, not saving to database: {}", mongoDto.name());
            }
        } catch (Exception e) {
            logger.error("Failed to save location to database: {}", mongoDto.name(), e);
            // Fehler beim Speichern sollte die API-Antwort nicht beeinträchtigen
        }
    }
}