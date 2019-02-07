package com.akurilo.weatherstation.service;

import dto.StationDto;

public interface WeatherConditionSevice {

    StationDto getCurrentWeatherCondition(String lat, String lon);
}
