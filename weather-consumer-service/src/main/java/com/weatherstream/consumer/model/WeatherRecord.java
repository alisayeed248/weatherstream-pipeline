package com.weatherstream.consumer.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_records")
public class WeatherRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "city", nullable = false, length = 100)
    private String city;
    
    @Column(name = "country", length = 3)
    private String country;
    
    @Column(name = "temperature", precision = 5, scale = 2)
    private BigDecimal temperature;
    
    @Column(name = "feels_like", precision = 5, scale = 2)
    private BigDecimal feelsLike;
    
    @Column(name = "humidity")
    private Integer humidity;
    
    @Column(name = "weather_main", length = 50)
    private String weatherMain;
    
    @Column(name = "weather_description")
    private String weatherDescription;
    
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
    
    @Column(name = "source", length = 50)
    private String source = "openweathermap";
    
    // Default constructor
    public WeatherRecord() {
        this.recordedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    
    public BigDecimal getFeelsLike() { return feelsLike; }
    public void setFeelsLike(BigDecimal feelsLike) { this.feelsLike = feelsLike; }
    
    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }
    
    public String getWeatherMain() { return weatherMain; }
    public void setWeatherMain(String weatherMain) { this.weatherMain = weatherMain; }
    
    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }
    
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}