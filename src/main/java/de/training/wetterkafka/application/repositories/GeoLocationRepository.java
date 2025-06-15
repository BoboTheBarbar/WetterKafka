package de.training.wetterkafka.application.repositories;

import de.training.wetterkafka.gateway.mongodb.GeoLocationMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeoLocationRepository extends MongoRepository<GeoLocationMongoDTO, String> {
    GeoLocationMongoDTO getByName(String name);
}
