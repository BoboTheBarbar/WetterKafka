package de.training.wetterkafka.application;

import de.training.wetterkafka.application.repositories.GeoLocationRepository;
import de.training.wetterkafka.domain.GeoLocationDomain;
import de.training.wetterkafka.gateway.mongodb.GeoLocationMongoAdapter;
import de.training.wetterkafka.gateway.mongodb.GeoLocationMongoDTO;
import org.springframework.stereotype.Component;

@Component
public class GeoLocationService {

    public static final GeoLocationMongoAdapter MONGO_ADAPTER = GeoLocationMongoAdapter.GEO_LOCATION_MONGO_ADAPTER;

    public GeoLocationService(GeoLocationRepository geoLocationRepository) {
        this.geoLocationRepository = geoLocationRepository;
    }

    GeoLocationRepository geoLocationRepository;

    public GeoLocationDomain save(GeoLocationDomain geoLocationDomain) {
        GeoLocationMongoDTO transformedGeoLocation = MONGO_ADAPTER.toGeoLocationMongoDTO(geoLocationDomain);
        GeoLocationMongoDTO savedGeoLocation = geoLocationRepository.save(transformedGeoLocation);

        return MONGO_ADAPTER.toGeoLocationDomain(savedGeoLocation);
    }

    public GeoLocationDomain findByName(String name) {
        GeoLocationMongoDTO mongoDTO = geoLocationRepository.getByName(name);
        return MONGO_ADAPTER.toGeoLocationDomain(mongoDTO);
    }
}
