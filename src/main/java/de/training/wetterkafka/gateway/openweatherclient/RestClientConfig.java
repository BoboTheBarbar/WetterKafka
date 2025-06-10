package de.training.wetterkafka.gateway.openweatherclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient openWeatherRestClient(OpenWeatherProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("User-Agent", "MyWeatherApp/1.0")
                .build();
    }
}
