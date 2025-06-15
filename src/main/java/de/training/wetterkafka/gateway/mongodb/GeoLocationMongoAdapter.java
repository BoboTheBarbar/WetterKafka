package de.training.wetterkafka.gateway.mongodb;

import de.training.wetterkafka.domain.GeoLocationDomain;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GeoLocationMongoAdapter {
    GeoLocationMongoAdapter INSTANCE = Mappers.getMapper(GeoLocationMongoAdapter.class);

    GeoLocationDomain toGeoLocationDomain(GeoLocationMongoDTO geoLocationMongoDTO);
    GeoLocationMongoDTO toGeoLocationMongoDTO(GeoLocationDomain geoLocationDomain);
}
