package com.weatherstream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherstream.model.WeatherData;
import com.weatherstream.service.WeatherService;

@RestController
public class WeatherController {

	private final WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Weather Service!";
	}

	@GetMapping("/weather")
	public WeatherData getWeather(@RequestParam String city) {
		return weatherService.getCurrentWeather(city);
	}

	@GetMapping("/health")
	public String health() {
		return "Weather service is running!";
	}
}
