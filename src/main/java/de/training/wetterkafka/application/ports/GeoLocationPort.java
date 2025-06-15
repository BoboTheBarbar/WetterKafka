package de.training.wetterkafka.application.ports;

import de.training.wetterkafka.domain.GeoLocationDomain;

/**
 * Port interface for GeoLocation persistence operations.
 * This interface belongs to the application layer and defines the contract
 * that the infrastructure layer must implement.
 */
public interface GeoLocationPort {

    GeoLocationDomain save(GeoLocationDomain geoLocationDomain);

    GeoLocationDomain findByName(String name);
}
