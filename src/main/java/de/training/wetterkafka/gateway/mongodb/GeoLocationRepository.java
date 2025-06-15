package de.training.wetterkafka.gateway.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeoLocationRepository extends MongoRepository<GeoLocationMongoDTO, String> {
    GeoLocationMongoDTO getByName(String name);
}
