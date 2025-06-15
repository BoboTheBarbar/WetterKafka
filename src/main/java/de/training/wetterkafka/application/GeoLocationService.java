package de.training.wetterkafka.application;

import de.training.wetterkafka.application.ports.GeoLocationPort;
import de.training.wetterkafka.domain.GeoLocationDomain;
import org.springframework.stereotype.Service;

/**
 * Application service for GeoLocation operations.
 * This service contains business logic and coordinates domain operations.
 * It depends only on domain objects and port interfaces.
 */
@Service
public class GeoLocationService {

    private final GeoLocationPort geoLocationPort;

    public GeoLocationService(GeoLocationPort geoLocationPort) {
        this.geoLocationPort = geoLocationPort;
    }

    public GeoLocationDomain save(GeoLocationDomain geoLocationDomain) {
        return geoLocationPort.save(geoLocationDomain);
    }

    public GeoLocationDomain findByName(String name) {
        return geoLocationPort.findByName(name);
    }
}
