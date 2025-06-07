package com.weatherstream.service;

import com.weatherstream.model.TrackedLocation;
import com.weatherstream.repository.TrackedLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicSchedulerService {
  /*
   * Right now, we need to get all cities from the db: especially at startup
   * For each city, we need a separate timer that runs every X minutes (for that
   * city's interval)
   * Keep track of the timers in a permanent map for cancelling or modifying
   * When a city is added or removed, we need to dynamically create and destroy
   * timers
   * Each timer will independently call the weather API
   */
  private static final Logger logger = LoggerFactory.getLogger(DynamicSchedulerService.class);

  private final TrackedLocationRepository trackedLocationRepository;
  private final WeatherService weatherService;
  private final TaskScheduler taskScheduler;

  // Map to store running timers: cityName --> timer
  private final Map<String, ScheduledFuture<?>> runningTimers = new ConcurrentHashMap<>();

  public DynamicSchedulerService(TrackedLocationRepository trackedLocationRepository, WeatherService weatherService) {
    this.trackedLocationRepository = trackedLocationRepository;
    this.weatherService = weatherService;
    this.taskScheduler = createTaskScheduler();
  }

  private TaskScheduler createTaskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(10); 
    scheduler.setThreadNamePrefix("weather-scheduler-"); 
    scheduler.initialize();
    return scheduler;
  }

  public void startCityTimer(TrackedLocation location) {
    String cityName = location.getCityName();

    // Don't create duplicate timers
    if (runningTimers.containsKey(cityName)) {
      logger.warn("Timer for {} already exists, skipping", cityName);
      return;
    }

    // Otherwise, we create the task that will run repeatedly
    Runnable weatherTask = () -> {
      try {
        logger.info("Fetching weather for {} (every {} minutes)", cityName, location.getIntervalMinutes());
        weatherService.ingestWeatherData(cityName);

        location.setLastFetchedAt(java.time.LocalDateTime.now());
        trackedLocationRepository.save(location);
      } catch (Exception e) {
        logger.error("Error fetching weather for {}: {}", cityName, e.getMessage());
      }
    };

    // Schedule task to run every X minutes
    Duration interval = Duration.ofMinutes(location.getIntervalMinutes());
    ScheduledFuture<?> timer = taskScheduler.scheduleAtFixedRate(weatherTask, interval);

    // Store timer to cancel later
    runningTimers.put(cityName, timer);
    logger.info("Started timer for {} - will fetch every {} minutes", cityName, location.getIntervalMinutes());
  }
}
