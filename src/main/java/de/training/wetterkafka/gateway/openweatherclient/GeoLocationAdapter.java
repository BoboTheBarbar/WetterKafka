package de.training.wetterkafka.gateway.openweatherclient;

import de.training.wetterkafka.domain.GeoLocationDomain;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Adapter für die Konvertierung zwischen GeoLocationMongoDTO und GeoLocationDomain
 */
@Component
public class GeoLocationAdapter {

    public GeoLocationDomain toDomain(GeoLocationOpenWeatherDTO mongoDto) {
        if (mongoDto == null) {
            return null;
        }

        return new GeoLocationDomain(
                mongoDto.name(),
                mongoDto.lat(),
                mongoDto.lon(),
                mongoDto.country(),
                mongoDto.state()
        );
    }

    public GeoLocationOpenWeatherDTO toMongoDto(GeoLocationDomain domain) {
        if (domain == null) {
            return null;
        }

        return new GeoLocationOpenWeatherDTO(
                domain.name(),
                domain.lat(),
                domain.lon(),
                domain.country(),
                domain.state()
        );
    }

    public List<GeoLocationDomain> toDomainList(List<GeoLocationOpenWeatherDTO> mongoDtos) {
        if (mongoDtos == null) {
            return Collections.emptyList();
        }

        return mongoDtos.stream()
                .map(this::toDomain)
                .toList();
    }

    public List<GeoLocationOpenWeatherDTO> toMongoDtoList(List<GeoLocationDomain> domains) {
        if (domains == null) {
            return Collections.emptyList();
        }

        return domains.stream()
                .map(this::toMongoDto)
                .toList();
    }

    public boolean isValidDomain(GeoLocationDomain domain) {
        if (domain == null) {
            return false;
        }

        return domain.name() != null && !domain.name().trim().isEmpty() &&
               domain.lat() >= -90.0 && domain.lat() <= 90.0 &&
               domain.lon() >= -180.0 && domain.lon() <= 180.0 &&
               domain.country() != null && !domain.country().trim().isEmpty();
    }

    public boolean isValidMongoDto(GeoLocationOpenWeatherDTO mongoDto) {
        if (mongoDto == null) {
            return false;
        }

        return mongoDto.name() != null && !mongoDto.name().trim().isEmpty() &&
               mongoDto.lat() >= -90.0 && mongoDto.lat() <= 90.0 &&
               mongoDto.lon() >= -180.0 && mongoDto.lon() <= 180.0 &&
               mongoDto.country() != null && !mongoDto.country().trim().isEmpty();
    }

    /**
     * Erstellt eine normalisierte Version eines Domain-Objekts
     * (Name trimmed, Country in Großbuchstaben)
     *
     * @param domain Das Domain-Objekt
     * @return Normalisiertes Domain-Objekt
     */
    public GeoLocationDomain normalize(GeoLocationDomain domain) {
        if (domain == null) {
            return null;
        }

        return new GeoLocationDomain(
                domain.name().trim(),
                domain.lat(),
                domain.lon(),
                domain.country().toUpperCase().trim(),
                domain.state() != null ? domain.state().trim() : null
        );
    }

    /**
     * Erstellt eine normalisierte Version eines MongoDB DTOs
     * (Name trimmed, Country in Großbuchstaben)
     *
     * @param mongoDto Das MongoDB DTO
     * @return Normalisiertes MongoDB DTO
     */
    public GeoLocationOpenWeatherDTO normalize(GeoLocationOpenWeatherDTO mongoDto) {
        if (mongoDto == null) {
            return null;
        }

        return new GeoLocationOpenWeatherDTO(
                mongoDto.name().trim(),
                mongoDto.lat(),
                mongoDto.lon(),
                mongoDto.country().toUpperCase().trim(),
                mongoDto.state() != null ? mongoDto.state().trim() : null
        );
    }
}
