package com.akurilo.weatherstation.service;


import dto.StationDto;
import enums.WindDirection;
import exception.OpenWeatherMapApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class WeatherConditionServiceImpl implements WeatherConditionSevice {

    private static final String API_URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={appid}";
    private static final String APP_ID = "d847f21e7b4c4d9f7e5938b3c1d7a002";
    private final RestTemplate restTemplate;

    @Override
    public StationDto getCurrentWeatherCondition(StationDto stationDto) {
        String lat = stationDto.getCoordinate().getLat();
        String lon = stationDto.getCoordinate().getLon();
        ResponseEntity<String> response = getRequestToWeatherApi(lat, lon);
        if (response.getStatusCode().is2xxSuccessful()) {
            stationDto.setTemperatureC(extractTemperature(response.getBody()));
            stationDto.setHumidity(extractHumidity(response.getBody()));
            stationDto.setPressure(extractPressure(response.getBody()));
            stationDto.setWindSpeed(extractWindSpeed(response.getBody()));
            stationDto.setWindDirection(extractWindDirection(response.getBody()));
            return stationDto;
        } else {
            throw new OpenWeatherMapApiException(response.getStatusCodeValue());
        }
    }

    private ResponseEntity<String> getRequestToWeatherApi(String lat, String lon) {
        Map<String, Object> uriParams = new HashMap<>(3);
        uriParams.put("lat", lat);
        uriParams.put("lon", lon);
        uriParams.put("appid", APP_ID);

        URI uri = UriComponentsBuilder.fromUriString(API_URL_TEMPLATE)
                .buildAndExpand(uriParams)
                .toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
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
        Pattern pattern = Pattern.compile("(?s).*(\"speed\":((\\d+.\\d+)|(\\w)))(.*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.matches()) {
            return Double.valueOf(matcher.group(2));
        }
        return null;
    }

    // not work now because Openweathermap change response
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
