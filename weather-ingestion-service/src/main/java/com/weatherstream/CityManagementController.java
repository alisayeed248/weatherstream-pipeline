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
    this.trackedLocationRepository = trackedLocationRepository;
  }

  @PostMapping("/validate")
  public ResponseEntity<String> validateCity(@RequestBody String cityName) {
    boolean isValid = weatherService.validateCity(cityName);
    return ResponseEntity.ok(isValid ? "Valid" : "Invalid");
  }

  @PostMapping 
  public ResponseEntity<String> addCity(@RequestBody AddCityRequest request) {
    try {
      // 1. Validate the city 
      if (!weatherService.validateCity(request.getCityName())) {
        return ResponseEntity.badRequest().body("Invalid city: " + request.getCityName());
      }

      // 2. Check if city already exists
      TrackedLocation existing = trackedLocationRepository.findByCityName(request.getCityName());
      if (existing != null) {
        return ResponseEntity.badRequest().body("City already being tracked: " + request.getCityName());
      }

      // 3. Create and save new tracked location
      TrackedLocation newLocation = new TrackedLocation(request.getCityName(), request.getIntervalMinutes());
      trackedLocationRepository.save(newLocation);

      // 4. Start timer immediately
      schedulerService.startCityTimer(newLocation);

      return ResponseEntity.ok("Successfully added " + request.getCityName() + " with " + request.getIntervalMinutes() + "-minute intervals");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Error adding city: " + e.getMessage());
    }
  }

}