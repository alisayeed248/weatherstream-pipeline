package com.weatherstream.service;

import com.weatherstream.model.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherService {

  private final WebClient webClient;
  private final String apiKey;
  private final String baseUrl = "http://api.openweathermap.org/data/2.5";
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

  public WeatherService(@Value("${openweather.api.key}") String apiKey, KafkaTemplate<String, String> kafkaTemplate) {
    this.apiKey = apiKey;
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = new ObjectMapper();
  }

  public String ingestWeatherData(String city) {
    try {
      // HTTP call to OpenWeatherMap API and parse into WeatherData object
      WeatherData weatherData = webClient.get().uri(uriBuilder -> uriBuilder.path("/weather").queryParam("q", city)
          .queryParam("appid", apiKey).queryParam("units", "metric").build()).retrieve().bodyToMono(WeatherData.class)
          .block();
      // Parse our response back to JSON
      String weatherJson = objectMapper.writeValueAsString(weatherData);
      // Send to Kafka with city as key
      kafkaTemplate.send("weather.raw", city, weatherJson);

      return "Weather data for " + city + " sent to Kafka topic: weather.raw";
    } catch (Exception e) {
      return "Error ingesting weather data: " + e.getMessage();
    }
  }

  @Scheduled(fixedRate = 60000)
  public void automaticWeatherFetch() {
    List<String> cities = Arrays.asList("London", "Tokyo", "Sydney", "NewYork", "Paris");
    logger.info("Starting automatic weather fetch for {} cities", cities.size());

    for (String city : cities) {
      logger.info("Fetching weather for {}", city);
      try {
        ingestWeatherData(city);
        logger.info("Successfully fetched weather data for {}", city);
      } catch (Exception e) {
        logger.error("Error automatically fetching weather data for {}: {}", city, e.getMessage());
      }
    }
    logger.info("Completed weather fetch cycle for all cities");
  }
}
