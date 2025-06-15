package de.training.wetterkafka.application;

import de.training.wetterkafka.application.repositories.GeoLocationRepository;
import de.training.wetterkafka.domain.GeoLocationDomain;
import org.springframework.stereotype.Component;

@Component
public class GeoLocationService {

    public GeoLocationService(GeoLocationRepository geoLocationRepository) {
        this.geoLocationRepository = geoLocationRepository;
    }

    GeoLocationRepository geoLocationRepository;

    public GeoLocationDomain save(GeoLocationDomain geoLocationDomain) {
        return geoLocationRepository.save(geoLocationDomain);
    }

    public GeoLocationDomain findByName(String name) {
        return geoLocationRepository.getByName(name);
    }
}
