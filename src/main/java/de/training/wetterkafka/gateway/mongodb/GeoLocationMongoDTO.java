package de.training.wetterkafka.gateway.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "geolocations")
public record GeoLocationMongoDTO(
        @Id  String id,
        String name,
        double lat,
        double lon,
        String country,
        String state
) {
}
