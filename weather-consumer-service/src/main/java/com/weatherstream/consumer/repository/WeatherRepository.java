package com.weatherstream.consumer.repository;

import com.weatherstream.consumer.model.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherRecord, Long> {
    
    List<WeatherRecord> findByCityOrderByRecordedAtDesc(String city);
    
    List<WeatherRecord> findByRecordedAtAfterOrderByRecordedAtDesc(LocalDateTime after);
    
    @Query("SELECT w FROM WeatherRecord w WHERE w.recordedAt > :since ORDER BY w.recordedAt DESC")
    List<WeatherRecord> findRecentWeather(LocalDateTime since);
}