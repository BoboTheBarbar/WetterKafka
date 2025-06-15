package de.training.wetterkafka.gateway.openweatherclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoLocationOpenWeatherDTO(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {
}
