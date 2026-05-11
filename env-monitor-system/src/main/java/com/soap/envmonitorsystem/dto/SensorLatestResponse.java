package com.soap.envmonitorsystem.dto;

import java.time.LocalDateTime;

public record SensorLatestResponse(
        Double lightLux,
        LocalDateTime lightAt,
        Double temperatureCelsius,
        LocalDateTime temperatureAt,
        Double gpsLatitude,
        Double gpsLongitude,
        LocalDateTime gpsAt,
        Double humidityPercent,
        LocalDateTime humidityAt,
        Double pressureHpa,
        LocalDateTime pressureAt
) {}
