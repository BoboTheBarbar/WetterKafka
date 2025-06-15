package de.training.wetterkafka.gateway.wettercontroller;

import de.training.wetterkafka.domain.GeoLocationDomain;
import de.training.wetterkafka.gateway.openweatherclient.GeoLocationOpenWeatherAdapter;
import de.training.wetterkafka.gateway.openweatherclient.GeoLocationOpenWeatherDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeoLocationOpenWeatherAdapterTest {

    private final GeoLocationOpenWeatherAdapter adapter = GeoLocationOpenWeatherAdapter.GEO_LOCATION_OPEN_WEATHER_ADAPTER;

    @Nested
    @DisplayName("MongoDB DTO to Domain Conversion")
    class MongoToDomainTests {

        @Test
        @DisplayName("Should convert valid MongoDB DTO to Domain")
        void shouldConvertValidMongoToDomain() {
            // Given
            GeoLocationOpenWeatherDTO mongoDto = new GeoLocationOpenWeatherDTO(
                    "Berlin",
                    52.5200,
                    13.4050,
                    "DE",
                    "Berlin"
            );

            // When
            GeoLocationDomain domain = adapter.toDomain(mongoDto);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.name()).isEqualTo("Berlin");
            assertThat(domain.lat()).isEqualTo(52.5200);
            assertThat(domain.lon()).isEqualTo(13.4050);
            assertThat(domain.country()).isEqualTo("DE");
            assertThat(domain.state()).isEqualTo("Berlin");
        }

        @Test
        @DisplayName("Should return null when MongoDB DTO is null")
        void shouldReturnNullWhenMongoDtoIsNull() {
            // When
            GeoLocationDomain domain = adapter.toDomain(null);

            // Then
            assertThat(domain).isNull();
        }

        @Test
        @DisplayName("Should convert MongoDB DTO with null state")
        void shouldConvertMongoWithNullState() {
            // Given
            GeoLocationOpenWeatherDTO mongoDto = new GeoLocationOpenWeatherDTO(
                    "Hamburg",
                    53.5511,
                    9.9937,
                    "DE",
                    null
            );

            // When
            GeoLocationDomain domain = adapter.toDomain(mongoDto);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.name()).isEqualTo("Hamburg");
            assertThat(domain.state()).isNull();
        }
    }

    @Nested
    @DisplayName("Domain to MongoDB DTO Conversion")
    class DomainToMongoTests {

        @Test
        @DisplayName("Should convert valid Domain to MongoDB DTO")
        void shouldConvertValidDomainToMongo() {
            // Given
            GeoLocationDomain domain = new GeoLocationDomain(
                    "Munich",
                    48.1351,
                    11.5820,
                    "DE",
                    "Bavaria"
            );

            // When
            GeoLocationOpenWeatherDTO mongoDto = adapter.toOpenWeatherDto(domain);

            // Then
            assertThat(mongoDto).isNotNull();
            assertThat(mongoDto.name()).isEqualTo("Munich");
            assertThat(mongoDto.lat()).isEqualTo(48.1351);
            assertThat(mongoDto.lon()).isEqualTo(11.5820);
            assertThat(mongoDto.country()).isEqualTo("DE");
            assertThat(mongoDto.state()).isEqualTo("Bavaria");
        }

        @Test
        @DisplayName("Should return null when Domain is null")
        void shouldReturnNullWhenDomainIsNull() {
            // When
            GeoLocationOpenWeatherDTO mongoDto = adapter.toOpenWeatherDto(null);

            // Then
            assertThat(mongoDto).isNull();
        }
    }

    @Nested
    @DisplayName("List Conversion Tests")
    class ListConversionTests {

        @Test
        @DisplayName("Should convert list of MongoDB DTOs to Domains")
        void shouldConvertMongoDtoListToDomainList() {
            // Given
            List<GeoLocationOpenWeatherDTO> mongoDtos = Arrays.asList(
                    new GeoLocationOpenWeatherDTO("Berlin", 52.5200, 13.4050, "DE", "Berlin"),
                    new GeoLocationOpenWeatherDTO("Hamburg", 53.5511, 9.9937, "DE", "Hamburg"),
                    new GeoLocationOpenWeatherDTO("Munich", 48.1351, 11.5820, "DE", "Bavaria")
            );

            // When
            List<GeoLocationDomain> domains = adapter.toDomainList(mongoDtos);

            // Then
            assertThat(domains).hasSize(3);
            assertThat(domains.get(0).name()).isEqualTo("Berlin");
            assertThat(domains.get(1).name()).isEqualTo("Hamburg");
            assertThat(domains.get(2).name()).isEqualTo("Munich");
        }

        @Test
        @DisplayName("Should convert list of Domains to MongoDB DTOs")
        void shouldConvertDomainListToMongoDtoList() {
            // Given
            List<GeoLocationDomain> domains = Arrays.asList(
                    new GeoLocationDomain("Frankfurt", 50.1109, 8.6821, "DE", "Hessen"),
                    new GeoLocationDomain("Cologne", 50.9375, 6.9603, "DE", "NRW")
            );

            // When
            List<GeoLocationOpenWeatherDTO> mongoDtos = adapter.toOpenWeatherDtoList(domains);

            // Then
            assertThat(mongoDtos).hasSize(2);
            assertThat(mongoDtos.get(0).name()).isEqualTo("Frankfurt");
            assertThat(mongoDtos.get(1).name()).isEqualTo("Cologne");
        }

        @Test
        @DisplayName("Should return null when list is null")
        void shouldReturnNullWhenListIsNull() {
            // When & Then
            assertThat(adapter.toDomainList(null)).isNull();
            assertThat(adapter.toOpenWeatherDtoList(null)).isNull();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate correct Domain object")
        void shouldValidateCorrectDomain() {
            // Given
            GeoLocationDomain validDomain = new GeoLocationDomain(
                    "Berlin",
                    52.5200,
                    13.4050,
                    "DE",
                    "Berlin"
            );

            // When & Then
            assertThat(adapter.isValidDomain(validDomain)).isTrue();
        }

        @Test
        @DisplayName("Should reject Domain with invalid coordinates")
        void shouldRejectDomainWithInvalidCoordinates() {
            // Given
            GeoLocationDomain invalidLatDomain = new GeoLocationDomain(
                    "Invalid",
                    91.0, // Invalid latitude
                    13.4050,
                    "DE",
                    "Berlin"
            );

            GeoLocationDomain invalidLonDomain = new GeoLocationDomain(
                    "Invalid",
                    52.5200,
                    181.0, // Invalid longitude
                    "DE",
                    "Berlin"
            );

            // When & Then
            assertThat(adapter.isValidDomain(invalidLatDomain)).isFalse();
            assertThat(adapter.isValidDomain(invalidLonDomain)).isFalse();
        }

        @Test
        @DisplayName("Should reject Domain with empty name or country")
        void shouldRejectDomainWithEmptyFields() {
            // Given
            GeoLocationDomain emptyNameDomain = new GeoLocationDomain(
                    "",
                    52.5200,
                    13.4050,
                    "DE",
                    "Berlin"
            );

            GeoLocationDomain emptyCountryDomain = new GeoLocationDomain(
                    "Berlin",
                    52.5200,
                    13.4050,
                    "",
                    "Berlin"
            );

            // When & Then
            assertThat(adapter.isValidDomain(emptyNameDomain)).isFalse();
            assertThat(adapter.isValidDomain(emptyCountryDomain)).isFalse();
        }

        @Test
        @DisplayName("Should validate MongoDB DTO correctly")
        void shouldValidateMongoDto() {
            // Given
            GeoLocationOpenWeatherDTO validDto = new GeoLocationOpenWeatherDTO(
                    "Hamburg",
                    53.5511,
                    9.9937,
                    "DE",
                    "Hamburg"
            );

            GeoLocationOpenWeatherDTO invalidDto = new GeoLocationOpenWeatherDTO(
                    null, // Invalid name
                    53.5511,
                    9.9937,
                    "DE",
                    "Hamburg"
            );

            // When & Then
            assertThat(adapter.isValidOpenWeatherDto(validDto)).isTrue();
            assertThat(adapter.isValidOpenWeatherDto(invalidDto)).isFalse();
        }
    }

    @Nested
    @DisplayName("Normalization Tests")
    class NormalizationTests {

        @Test
        @DisplayName("Should normalize Domain object")
        void shouldNormalizeDomain() {
            // Given
            GeoLocationDomain unnormalizedDomain = new GeoLocationDomain(
                    "  Berlin  ",
                    52.5200,
                    13.4050,
                    "de",
                    "  berlin  "
            );

            // When
            GeoLocationDomain normalizedDomain = adapter.normalize(unnormalizedDomain);

            // Then
            assertThat(normalizedDomain.name()).isEqualTo("Berlin");
            assertThat(normalizedDomain.country()).isEqualTo("DE");
            assertThat(normalizedDomain.state()).isEqualTo("berlin");
        }

        @Test
        @DisplayName("Should normalize MongoDB DTO")
        void shouldNormalizeMongoDto() {
            // Given
            GeoLocationOpenWeatherDTO unnormalizedDto = new GeoLocationOpenWeatherDTO(
                    "  Hamburg  ",
                    53.5511,
                    9.9937,
                    "de",
                    "  hamburg  "
            );

            // When
            GeoLocationOpenWeatherDTO normalizedDto = adapter.normalize(unnormalizedDto);

            // Then
            assertThat(normalizedDto.name()).isEqualTo("Hamburg");
            assertThat(normalizedDto.country()).isEqualTo("DE");
            assertThat(normalizedDto.state()).isEqualTo("hamburg");
        }

        @Test
        @DisplayName("Should handle null state in normalization")
        void shouldHandleNullStateInNormalization() {
            // Given
            GeoLocationDomain domainWithNullState = new GeoLocationDomain(
                    "Munich",
                    48.1351,
                    11.5820,
                    "de",
                    null
            );

            // When
            GeoLocationDomain normalizedDomain = adapter.normalize(domainWithNullState);

            // Then
            assertThat(normalizedDomain.state()).isNull();
            assertThat(normalizedDomain.country()).isEqualTo("DE");
        }
    }

    @Nested
    @DisplayName("Bidirectional Conversion Tests")
    class BidirectionalTests {

        @Test
        @DisplayName("Should maintain data integrity in round-trip conversion")
        void shouldMaintainDataIntegrityInRoundTripConversion() {
            // Given
            GeoLocationDomain originalDomain = new GeoLocationDomain(
                    "Stuttgart",
                    48.7758,
                    9.1829,
                    "DE",
                    "Baden-Württemberg"
            );

            // When - Round trip: Domain -> MongoDB DTO -> Domain
            GeoLocationOpenWeatherDTO mongoDto = adapter.toOpenWeatherDto(originalDomain);
            GeoLocationDomain convertedBackDomain = adapter.toDomain(mongoDto);

            // Then
            assertThat(convertedBackDomain).isEqualTo(originalDomain);
        }

        @Test
        @DisplayName("Should maintain data integrity in reverse round-trip conversion")
        void shouldMaintainDataIntegrityInReverseRoundTripConversion() {
            // Given
            GeoLocationOpenWeatherDTO originalMongoDto = new GeoLocationOpenWeatherDTO(
                    "Dresden",
                    51.0504,
                    13.7373,
                    "DE",
                    "Saxony"
            );

            // When - Round trip: MongoDB DTO -> Domain -> MongoDB DTO
            GeoLocationDomain domain = adapter.toDomain(originalMongoDto);
            GeoLocationOpenWeatherDTO convertedBackMongoDto = adapter.toOpenWeatherDto(domain);

            // Then
            assertThat(convertedBackMongoDto).isEqualTo(originalMongoDto);
        }
    }
}
