package de.training.wetterkafka.gateway.mongodb;

import de.training.wetterkafka.application.ports.GeoLocationPort;
import de.training.wetterkafka.domain.GeoLocationDomain;
import org.springframework.stereotype.Component;

/**
 * MongoDB implementation of the GeoLocationPort.
 * This adapter translates between the domain layer and MongoDB infrastructure.
 */
@Component
public class GeoLocationPortImpl implements GeoLocationPort {

    private final GeoLocationRepository geoLocationRepository;
    private final GeoLocationMongoAdapter mongoAdapter;

    public GeoLocationPortImpl(GeoLocationRepository geoLocationRepository) {
        this.geoLocationRepository = geoLocationRepository;
        this.mongoAdapter = GeoLocationMongoAdapter.INSTANCE;
    }

    @Override
    public GeoLocationDomain save(GeoLocationDomain geoLocationDomain) {
        GeoLocationMongoDTO mongoDTO = mongoAdapter.toGeoLocationMongoDTO(geoLocationDomain);
        GeoLocationMongoDTO savedDTO = geoLocationRepository.save(mongoDTO);

        return mongoAdapter.toGeoLocationDomain(savedDTO);
    }

    @Override
    public GeoLocationDomain findByName(String name) {
        // Find using repository
        GeoLocationMongoDTO mongoDTO = geoLocationRepository.getByName(name);
        
        // Convert to domain (MapStruct handles null check)
        return mongoAdapter.toGeoLocationDomain(mongoDTO);
    }
}
