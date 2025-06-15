package de.training.wetterkafka.application.repositories;

import de.training.wetterkafka.domain.GeoLocationDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeoLocationRepository extends MongoRepository<GeoLocationDomain, String> {
    GeoLocationDomain getByName(String name);
}
