package com.weatherstream.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracked_locations")
public class TrackedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "city_name", nullable = false)
    private String cityName;
    
    @Column(name = "interval_minutes", nullable = false)
    private Integer intervalMinutes;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_fetched_at")
    private LocalDateTime lastFetchedAt;
    
    // Default constructor
    public TrackedLocation() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Constructor
    public TrackedLocation(String cityName, Integer intervalMinutes) {
        this();
        this.cityName = cityName;
        this.intervalMinutes = intervalMinutes;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public Integer getIntervalMinutes() { return intervalMinutes; }
    public void setIntervalMinutes(Integer intervalMinutes) { this.intervalMinutes = intervalMinutes; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastFetchedAt() { return lastFetchedAt; }
    public void setLastFetchedAt(LocalDateTime lastFetchedAt) { this.lastFetchedAt = lastFetchedAt; }
}