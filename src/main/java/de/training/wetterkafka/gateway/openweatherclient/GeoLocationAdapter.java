package de.training.wetterkafka.gateway.openweatherclient;

import de.training.wetterkafka.domain.GeoLocationDomain;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * MapStruct-Adapter für die Konvertierung zwischen GeoLocationOpenWeatherDTO und GeoLocationDomain
 */
@Mapper
public interface GeoLocationAdapter {

    GeoLocationAdapter INSTANCE = Mappers.getMapper(GeoLocationAdapter.class);

    // Basic mapping methods - MapStruct will generate these
    GeoLocationDomain toDomain(GeoLocationOpenWeatherDTO openWeatherDto);
    
    GeoLocationOpenWeatherDTO toOpenWeatherDto(GeoLocationDomain domain);
    
    List<GeoLocationDomain> toDomainList(List<GeoLocationOpenWeatherDTO> openWeatherDtos);
    
    List<GeoLocationOpenWeatherDTO> toOpenWeatherDtoList(List<GeoLocationDomain> domains);

    default boolean isValidDomain(GeoLocationDomain domain) {
        if (domain == null) {
            return false;
        }

        return StringUtils.hasText(domain.name()) &&
               domain.lat() >= -90.0 && domain.lat() <= 90.0 &&
               domain.lon() >= -180.0 && domain.lon() <= 180.0 &&
               StringUtils.hasText(domain.country());
    }

    default boolean isValidOpenWeatherDto(GeoLocationOpenWeatherDTO openWeatherDto) {
        if (openWeatherDto == null) {
            return false;
        }

        return StringUtils.hasText(openWeatherDto.name()) &&
               openWeatherDto.lat() >= -90.0 && openWeatherDto.lat() <= 90.0 &&
               openWeatherDto.lon() >= -180.0 && openWeatherDto.lon() <= 180.0 &&
               StringUtils.hasText(openWeatherDto.country());
    }

    default GeoLocationDomain normalize(GeoLocationDomain domain) {
        if (domain == null) {
            return null;
        }
        
        return new GeoLocationDomain(
                domain.name() != null ? domain.name().trim() : null,
                domain.lat(),
                domain.lon(),
                domain.country() != null ? domain.country().toUpperCase().trim() : null,
                domain.state() != null ? domain.state().trim() : null
        );
    }

    default GeoLocationOpenWeatherDTO normalize(GeoLocationOpenWeatherDTO openWeatherDto) {
        if (openWeatherDto == null) {
            return null;
        }
        
        return new GeoLocationOpenWeatherDTO(
                openWeatherDto.name() != null ? openWeatherDto.name().trim() : null,
                openWeatherDto.lat(),
                openWeatherDto.lon(),
                openWeatherDto.country() != null ? openWeatherDto.country().toUpperCase().trim() : null,
                openWeatherDto.state() != null ? openWeatherDto.state().trim() : null
        );
    }
}
