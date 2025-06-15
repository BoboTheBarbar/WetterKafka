package de.training.wetterkafka.gateway.wettercontroller;

import de.training.wetterkafka.application.GeoLocationService;
import de.training.wetterkafka.application.repositories.GeoLocationRepository;
import de.training.wetterkafka.domain.GeoLocationDomain;
import de.training.wetterkafka.gateway.openweatherclient.GeoLocationAdapter;
import de.training.wetterkafka.gateway.openweatherclient.OpenWeatherProperties;
import de.training.wetterkafka.gateway.openweatherclient.RestClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WetterController.class)
@Import({RestClientConfig.class, OpenWeatherProperties.class, GeoLocationService.class, GeoLocationAdapter.class})
class WetterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Option 1: Bean aus dem Application Context beziehen und als Spy verwenden
    @MockitoSpyBean
    private GeoLocationService geoLocationService;

    // Repository mocken, da es von GeoLocationService verwendet wird
    @MockitoBean
    private GeoLocationRepository geoLocationRepository;

    @Test
    void getWetterByKnownCity() throws Exception {
        // Given
        String cityName = "Bonn";
        GeoLocationDomain mockDomain = new GeoLocationDomain(cityName, 50.7374, 7.0982, "DE", "NRW");
        when(geoLocationRepository.getByName(cityName)).thenReturn(mockDomain);

        // When & Than
        mockMvc.perform(get("/weather/" + cityName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(cityName))
                .andReturn();

    }
}
