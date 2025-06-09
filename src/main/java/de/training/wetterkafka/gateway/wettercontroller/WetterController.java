package de.training.wetterkafka.gateway.wettercontroller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
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

    // TODO: extract Weather client
    // TODO: extract keys and urls in properties
    private final RestClient restClient;

    private static final Logger logger = LogManager.getLogger(WetterController.class);

    public WetterController() {
        this.restClient = RestClient.builder().baseUrl("http://api.openweathermap.org").build();
    }

    @GetMapping("/weather/{cityName}")
    public ResponseEntity<JsonNode> weather(@PathVariable String cityName) {
        String apiKey = "f67c2dad94e8ba5c46368c818b7796ac";
        List<GeoLocation> geolocations = getGeolocationsByCityName(cityName);
        GeoLocation firstGeolocation = geolocations.get(0);
        JsonNode jsonNode = restClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path("/data/2.5/weather")
                                .queryParam("lat", firstGeolocation.lat())
                                .queryParam("lon", firstGeolocation.lon())
                                .queryParam("appid", apiKey)
                                .build())
                .retrieve().body(JsonNode.class);

        return ResponseEntity.ok(jsonNode);
    }

    private List<GeoLocation> getGeolocationsByCityName(String cityName) {
        String apiKey = "f67c2dad94e8ba5c46368c818b7796ac";
        // TODO: Handle locations == null
        GeoLocation[] locations = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/direct")
                        .queryParam("q", cityName)
                        .queryParam("limit", 5)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .body(GeoLocation[].class);

        logger.info("Retrieved %d geolocations".formatted(locations.length));

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