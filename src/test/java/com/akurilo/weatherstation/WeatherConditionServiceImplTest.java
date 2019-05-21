package com.akurilo.weatherstation;

import com.akurilo.weatherstation.service.WeatherConditionServiceImpl;
import dto.CoordinateDto;
import dto.StationDto;
import enums.WindDirection;
import exception.OpenWeatherMapApiException;
import org.apache.tomcat.util.buf.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherConditionServiceImplTest {

    private static final String LAT_TEST = "00.00";
    private static final String LON_TEST = "00.00";
    private static final String URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    private static final String APP_ID = "d847f21e7b4c4d9f7e5938b3c1d7a002";
    private static final String URL_TEST = String.format(URL_TEMPLATE, LAT_TEST, LON_TEST, APP_ID);
    private static final String RESPONSE_API_TEMPLATE = "{\n" +
            "  \"coord\":{\n" +
            "    \"lon\":0,\n" +
            "    \"lat\":0\n" +
            "  },\n" +
            "  \"weather\":[\n" +
            "    {\n" +
            "      \"id\":800,\n" +
            "      \"main\":\"Clear\",\n" +
            "      \"description\":\"clear sky\",\n" +
            "      \"icon\":\"01d\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"base\":\"stations\",\n" +
            "  \"main\":{\n" +
            "    \"temp\":%temperatureTemplatePlace%,\n" +
            "    \"pressure\":%pressureTemplatePlace%,\n" +
            "    \"humidity\":%humidityTemplatePlace%,\n" +
            "    \"temp_min\":300.883,\n" +
            "    \"temp_max\":300.883,\n" +
            "    \"sea_level\":1022.82,\n" +
            "    \"grnd_level\":1022.8\n" +
            "  },\n" +
            "  \"wind\":{\n" +
            "    \"speed\":%windSpeedTemplatePlace%,\n" +
            "    \"deg\":%windDirectionTemplatePlace%\n" +
            "  },\n" +
            "  \"clouds\":{\n" +
            "    \"all\":0\n" +
            "  },\n" +
            "  \"dt\":1548855469,\n" +
            "  \"sys\":{\n" +
            "    \"message\":0.003,\n" +
            "    \"sunrise\":1548828588,\n" +
            "    \"sunset\":1548872212\n" +
            "  },\n" +
            "  \"id\":6295630,\n" +
            "  \"name\":\"Earth\",\n" +
            "  \"cod\":200\n" +
            "}";

    @InjectMocks
    private WeatherConditionServiceImpl weatherConditionServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    private String getValidResponse() {
        Map<String, String> testValues = new HashMap<>();
        testValues.put("temperatureTemplatePlace", "273.15");
        testValues.put("humidityTemplatePlace", "100");
        testValues.put("pressureTemplatePlace", "100");
        testValues.put("windSpeedTemplatePlace", "3.2");
        testValues.put("windDirectionTemplatePlace", "0");

        Pattern pattern = Pattern.compile("%(" + StringUtils.join(testValues.keySet(), '|') + ")%");
        Matcher matcher = pattern.matcher(RESPONSE_API_TEMPLATE);

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, testValues.get(matcher.group(1)));
        }
        return matcher.appendTail(stringBuffer).toString();
    }

    @Test
    public void shouldReturnCurrentWeatherCondition() {
        HttpHeaders headers = new HttpHeaders();
        when(this.restTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.<Class<String>>any())
        )
                .thenReturn(new ResponseEntity<>(getValidResponse(), headers, HttpStatus.OK));

        CoordinateDto coordinateDto = new CoordinateDto();
        coordinateDto.setLat(LAT_TEST);
        coordinateDto.setLon(LON_TEST);
        StationDto stationDto = new StationDto();
        stationDto.setCoordinate(coordinateDto);

        StationDto dto = weatherConditionServiceImpl.getCurrentWeatherCondition(stationDto);
        assertThat(dto.getTemperatureC() == 0).isTrue();
        assertThat(dto.getHumidity() == 100).isTrue();
        assertThat(dto.getPressure() == 100).isTrue();
        assertThat(dto.getWindSpeed() == 3.2).isTrue();
        assertThat(dto.getWindDirection().equals(WindDirection.NORTH)).isTrue();

        verify(restTemplate).exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.<Class<String>>any());
    }

    @Test(expected = OpenWeatherMapApiException.class)
    public void shouldReturnOpenWeatherMapApiException() {
        StationDto stationDto = new StationDto();
        CoordinateDto coordinate = new CoordinateDto();
        coordinate.setLat(LAT_TEST);
        coordinate.setLon(LON_TEST);
        stationDto.setCoordinate(coordinate);

        HttpHeaders headers = new HttpHeaders();

        when(this.restTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.<Class<String>>any())
        )
                .thenReturn(new ResponseEntity<>(getValidResponse(), headers, HttpStatus.BAD_REQUEST));

        weatherConditionServiceImpl.getCurrentWeatherCondition(stationDto);
    }
}
