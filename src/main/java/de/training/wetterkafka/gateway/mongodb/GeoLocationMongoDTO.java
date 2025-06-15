package de.training.wetterkafka.gateway.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "geolocations")
public record GeoLocationMongoDTO(
        String name,
        double lat,
        double lon,
        String country,
        String state
) {
}
