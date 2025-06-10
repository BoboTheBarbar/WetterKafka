package de.training.wetterkafka.gateway.wettercontroller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import de.training.wetterkafka.gateway.openweatherclient.OpenWeatherProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@RestController
public class WetterController {
    private final RestClient restClient;
    private final OpenWeatherProperties properties;

    private static final Logger logger = LogManager.getLogger(WetterController.class);

    public WetterController(RestClient restClient, OpenWeatherProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    @GetMapping("/weather/{cityName}")
    public ResponseEntity<JsonNode> weather(@PathVariable String cityName) {
        GeoLocation firstGeolocation = getGeolocationsByCityName(cityName).getFirst();
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

    private List<GeoLocation> getGeolocationsByCityName(String cityName) {
        // TODO: Handle locations == null
        GeoLocation[] locations =
            restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(properties.getGeolocationByCityEndpoint())
                            .queryParam("q", cityName)
                            .queryParam("limit", 5)
                            .queryParam("appid", properties.getApiKey())
                            .build())
                    .retrieve()
                    .body(GeoLocation[].class);

        String formatted = "Retrieved %d geolocations".formatted(locations.length);
        logger.info(formatted);

        return Arrays.asList(locations);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record GeoLocation(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {}