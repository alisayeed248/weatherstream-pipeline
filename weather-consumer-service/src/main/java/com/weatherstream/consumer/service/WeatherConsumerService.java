package com.weatherstream.consumer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherstream.consumer.model.WeatherRecord;
import com.weatherstream.consumer.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WeatherConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherConsumerService.class);
    
    private final WeatherRepository weatherRepository;
    private final ObjectMapper objectMapper;
    
    public WeatherConsumerService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
        this.objectMapper = new ObjectMapper();
    }
    
    @KafkaListener(topics = "weather.raw", groupId = "weather-consumer-group")
    public void consumeWeatherData(String weatherJson) {
        try {
            logger.info("Received weather data: {}", weatherJson);
            
            // Parse JSON from Kafka
            JsonNode weatherNode = objectMapper.readTree(weatherJson);
            
            // Create WeatherRecord from JSON
            WeatherRecord record = new WeatherRecord();
            record.setCity(weatherNode.get("name").asText());
            
            // Extract temperature data
            JsonNode mainNode = weatherNode.get("main");
            if (mainNode != null) {
                record.setTemperature(BigDecimal.valueOf(mainNode.get("temp").asDouble()));
                record.setHumidity(mainNode.get("humidity").asInt());
                
                // Handle feels_like if present
                if (mainNode.has("feels_like")) {
                    record.setFeelsLike(BigDecimal.valueOf(mainNode.get("feels_like").asDouble()));
                }
            }
            
            // Extract weather description
            JsonNode weatherArray = weatherNode.get("weather");
            if (weatherArray != null && weatherArray.isArray() && weatherArray.size() > 0) {
                JsonNode weather = weatherArray.get(0);
                record.setWeatherMain(weather.get("main").asText());
                record.setWeatherDescription(weather.get("description").asText());
            }
            
            // Save to database
            WeatherRecord saved = weatherRepository.save(record);
            logger.info("Saved weather record: {} - {}°C", saved.getCity(), saved.getTemperature());
            
        } catch (Exception e) {
            logger.error("Error processing weather data: {}", e.getMessage(), e);
        }
    }
}