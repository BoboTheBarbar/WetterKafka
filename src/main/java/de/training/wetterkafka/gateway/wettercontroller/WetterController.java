package de.training.wetterkafka.gateway.wettercontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WetterController {

    @GetMapping("/weather/{city}")
    public WeatherDTO weather(@PathVariable String city) {
        return new WeatherDTO(city);
    }
}
