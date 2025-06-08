package com.weatherstream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherstream.model.WeatherData;
import com.weatherstream.model.TrackedLocation;
import com.weatherstream.service.WeatherService;
import com.weatherstream.repository.TrackedLocationRepository;
import java.util.List;

@RestController
public class WeatherController {

	private final WeatherService weatherService;
	private final TrackedLocationRepository trackedLocationRepository;

	public WeatherController(WeatherService weatherService, TrackedLocationRepository trackedLocationRepository) {
		this.weatherService = weatherService;
		this.trackedLocationRepository = trackedLocationRepository;
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Weather Service!";
	}

	@GetMapping("/weather")
	public String getWeather(@RequestParam String city) {
		return weatherService.ingestWeatherData(city);
	}

	@GetMapping("/health")
	public String health() {
		return "Weather service is running!";
	}

	@GetMapping("/test-db")
	public List<TrackedLocation> testDatabase() {
		return trackedLocationRepository.findByIsActiveTrue();
	}

	@GetMapping("/validate")
	public String validateCity(@RequestParam String city) {
		boolean isValid = weatherService.validateCity(city);
		return isValid ? "Valid city: " + city : "Invalid city: " + city;
	}
}
