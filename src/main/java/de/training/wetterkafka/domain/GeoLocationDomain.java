package de.training.wetterkafka.domain;

public record GeoLocationDomain(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {
}
