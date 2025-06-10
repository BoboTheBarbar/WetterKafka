package de.training.wetterkafka.gateway.openweatherclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "openweather")
public class OpenWeatherProperties {
    private String apiKey = "f67c2dad94e8ba5c46368c818b7796ac";
    private String baseUrl = "http://api.openweathermap.org";
    private String weatherByGeolocationEndpoint = "/data/2.5/weather";
    private String geolocationByCityEndpoint = "/geo/1.0/direct";
}
