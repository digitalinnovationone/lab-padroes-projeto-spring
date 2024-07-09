package one.digitalinnovation.gof.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.digitalinnovation.gof.model.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient webClientNominatim;
    private final WebClient webClientOpenMeteo;
    private final ObjectMapper objectMapper;

    public WeatherService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientNominatim = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
        this.webClientOpenMeteo = webClientBuilder.baseUrl("https://api.open-meteo.com/v1/forecast").build();
        this.objectMapper = objectMapper;
    }

    public Mono<WeatherResponse> getWeatherByUF(String uf) {
        return getCoordinatesByUF(uf)
                .flatMap(coordinates -> getWeatherByCoordinates(coordinates[0], coordinates[1]));
    }

    private Mono<double[]> getCoordinatesByUF(String uf) {
        return webClientNominatim.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", uf + ", Brazil")
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        double lat = jsonNode.get(0).get("lat").asDouble();
                        double lon = jsonNode.get(0).get("lon").asDouble();
                        return new double[]{lat, lon};
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao processar resposta JSON", e);
                    }
                });
    }

    private Mono<WeatherResponse> getWeatherByCoordinates(double latitude, double longitude) {
        return webClientOpenMeteo.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current_weather", true)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        JsonNode currentWeather = jsonNode.get("current_weather");
                        String time = currentWeather.get("time").asText();
                        double temperature = currentWeather.get("temperature").asDouble();
                        double windspeed = currentWeather.get("windspeed").asDouble();
                        return new WeatherResponse(time, temperature, windspeed, latitude, longitude);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao processar resposta JSON", e);
                    }
                });
    }
}

