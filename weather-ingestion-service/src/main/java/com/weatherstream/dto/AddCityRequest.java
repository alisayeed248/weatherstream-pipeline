package com.weatherstream.dto;

public class AddCityRequest {
  private String cityName;
  private Integer intervalMinutes;

  public AddCityRequest() {
  }

  public AddCityRequest(String cityName, Integer intervalMinutes) {
    this.cityName = cityName;
    this.intervalMinutes = intervalMinutes;
  }

  // Getters and setters
  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Integer getIntervalMinutes() {
    return intervalMinutes;
  }

  public void setIntervalMinutes(Integer intervalMinutes) {
    this.intervalMinutes = intervalMinutes;
  }

}
