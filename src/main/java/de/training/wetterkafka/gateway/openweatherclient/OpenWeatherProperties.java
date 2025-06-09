package de.training.wetterkafka.gateway.openweatherclient;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
//@ConfigurationProperties(prefix = "openweather")
// TODO: make use of application properties
public class OpenWeatherProperties {
    private final String apiKey = "f67c2dad94e8ba5c46368c818b7796ac";
    private final String baseUrl = "http://api.openweathermap.org";
}
