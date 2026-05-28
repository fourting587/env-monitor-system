package com.soap.envmonitorsystem2.dto;

public record SensorLatestResponse(
        Double lightLux,
        Double temperatureCelsius,
        Double humidityPercent,
        Double pressureHpa,
        Double gpsLatitude,
        Double gpsLongitude,
        String lightAt,
        String temperatureAt,
        String humidityAt,
        String pressureAt,
        String gpsAt
) {
}
