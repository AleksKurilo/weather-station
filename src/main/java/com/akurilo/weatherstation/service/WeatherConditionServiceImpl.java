package com.akurilo.weatherstation.service;


import dto.StationDto;
import enums.WindDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import exception.OpenWeatherMapApiException;

@Component
@RequiredArgsConstructor
public class WeatherConditionServiceImpl implements WeatherConditionSevice {

    private static final String API_URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    private static final String APP_ID = "d847f21e7b4c4d9f7e5938b3c1d7a002";
    private final RestTemplate restTemplate;

    @Override
    public StationDto getCurrentWeatherCondition(String lat, String lon) {
        ResponseEntity<String> response = getRequestToWeatherApi(lat, lon);
        if (response.getStatusCode().is2xxSuccessful()) {
            StationDto stationDto = new StationDto();
            stationDto.setTemperatureC(extractTemperature(response.getBody()));
            stationDto.setHumidity(extractHumidity(response.getBody()));
            stationDto.setPressure(extractPressure(response.getBody()));
            stationDto.setWindSpeed(extractWindSpeed(response.getBody()));
            stationDto.setWindDirection(extractWindDirection(response.getBody()));
            return stationDto;
        } else {
            //TODO add exception
            //throw new OpenWeatherMapApiException(response.getStatusCodeValue());
        }
        return null;
    }

    private ResponseEntity<String> getRequestToWeatherApi(String lat, String lon) {
        String url = String.format(API_URL_TEMPLATE, lat, lon, APP_ID);
        HttpEntity<String> entity = new HttpEntity<>("");
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    private Double extractTemperature(String responseBody) {
        Pattern pattern = Pattern.compile("(?s).*(\"temp\":(\\d+.\\d+))(.*)");
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.matches()) {
            Double temperatureKelvin = Double.valueOf(matcher.group(2));
            return temperatureKelvin - 273.15;
        }
        return null;
    }

    private Double extractHumidity(String json) {
        Pattern pattern = Pattern.compile("(?s).*(\"humidity\":(\\d+))(.*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.matches()) {
            return Double.valueOf(matcher.group(2));
        }
        return null;
    }

    private Integer extractPressure(String json) {
        Pattern pattern = Pattern.compile("(?s).*(\"pressure\":(\\d+))(.*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.matches()) {
            return Integer.valueOf(matcher.group(2));
        }
        return null;
    }

    private Double extractWindSpeed(String json) {
        Pattern pattern = Pattern.compile("(?s).*(\"speed\":(\\d+.\\d+))(.*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.matches()) {
            return Double.valueOf(matcher.group(2));
        }
        return null;
    }

    private WindDirection extractWindDirection(String json) {
        Pattern pattern = Pattern.compile("(?s).*(\"deg\":(\\d+))(.*)");
        Matcher matcher = pattern.matcher(json);
        Integer directionAngle = null;
        if (matcher.matches()) {
            directionAngle = Integer.valueOf(matcher.group(2));
        }
        if (directionAngle == null) {
            return null;
        }
        if (45 > directionAngle || directionAngle > 315) {
            return WindDirection.NORTH;
        }
        if (45 < directionAngle && directionAngle < 135) {
            return WindDirection.EAST;
        }
        if (135 < directionAngle && directionAngle < 225) {
            return WindDirection.SOUTH;
        }
        if (225 < directionAngle && directionAngle < 315) {
            return WindDirection.WEST;
        }
        return null;
    }
}
