package com.weatherstream.service;

import com.weatherstream.model.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {

  private final WebClient webClient;
  private final String apiKey;
  private final String baseUrl = "http://api.openweathermap.org/data/2.5";

  public WeatherService(@Value("${openweather.api.key}") String apiKey) {
    this.apiKey = apiKey;
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
  }

  public WeatherData getCurrentWeather(String city) {
    return webClient.get().uri(uriBuilder -> uriBuilder.path("/weather").queryParam("q", city)
        .queryParam("appid", apiKey).queryParam("units", "metric").build()).retrieve().bodyToMono(WeatherData.class)
        .block();
  }
}
