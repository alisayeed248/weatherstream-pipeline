package com.weatherstream.consumer.controller;

import com.weatherstream.consumer.model.WeatherRecord;
import com.weatherstream.consumer.repository.WeatherRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WeatherQueryController {
  private final WeatherRepository weatherRepository;
  public WeatherQueryController(WeatherRepository weatherRepository) {
    this.weatherRepository = weatherRepository;
  }

  @GetMapping("/cities")
  public List<String> getCities() {
    return weatherRepository.findAllCities();
  }

  @GetMapping("/weather/{city}")
  public List<WeatherRecord> getWeatherForCity(@PathVariable String city, @RequestParam(defaultValue = "10") int limit) {
    return weatherRepository.findByCityOrderByRecordedAtDesc(city).stream().limit(limit).collect(Collectors.toList());
  }

  @GetMapping("/weather/{city}/recent")
  public List<WeatherRecord> getRecentWeatherForCity(@PathVariable String city, @RequestParam(defaultValue = "24") int hours) {
    LocalDateTime since = LocalDateTime.now().minusHours(hours);
    return weatherRepository.findByCityAndRecordedAtAfterOrderByRecordedAtDesc(city, since);
  }
}
