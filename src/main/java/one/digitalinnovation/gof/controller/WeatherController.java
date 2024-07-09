package one.digitalinnovation.gof.controller;

import one.digitalinnovation.gof.model.WeatherResponse;
import one.digitalinnovation.gof.service.impl.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/{uf}")
    public Mono<WeatherResponse> getWeather(@PathVariable String uf) {
        return weatherService.getWeatherByUF(uf);
    }
}