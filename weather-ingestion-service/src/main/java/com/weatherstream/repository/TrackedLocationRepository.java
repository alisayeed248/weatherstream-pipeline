package com.weatherstream.repository;

import com.weatherstream.model.TrackedLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrackedLocationRepository extends JpaRepository<TrackedLocation, Long> {
  List<TrackedLocation> findByIsActiveTrue();
  TrackedLocation findByCityName(String cityName);
}
