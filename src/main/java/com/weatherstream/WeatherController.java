package com.weatherstream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello from Weather Service!";
	}
	
	@GetMapping("/weather")
	public String getWeather() {
		return "Weather data goes here.";
	}
	
	@GetMapping("/health")
	public String health() {
		return "Weather service is running!";
	}
}
