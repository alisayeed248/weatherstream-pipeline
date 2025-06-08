package com.weatherstream;

import com.weatherstream.dto.AddCityRequest;
import com.weatherstream.model.TrackedLocation;
import com.weatherstream.service.WeatherService;
import com.weatherstream.service.DynamicSchedulerService;
import com.weatherstream.repository.TrackedLocationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/cities")
public class CityManagementController {
  private final WeatherService weatherService;
  private final DynamicSchedulerService schedulerService;
  private final TrackedLocationRepository trackedLocationRepository;

  public CityManagementController(WeatherService weatherService, DynamicSchedulerService schedulerService,
      TrackedLocationRepository trackedLocationRepository) {
    this.weatherService = weatherService;
    this.schedulerService = schedulerService;
    this.trackedlocationRepository = trackedlocationRepository;
  }

  @PostMapping("/validate")
  public ResponseEntity<String> validateCity(@RequestBody String cityName) {
    boolean isValid = weatherService.validateCity(cityName);
    return ResponseEntity.ok(isValid ? "Valid" : "Invalid");
  }

}