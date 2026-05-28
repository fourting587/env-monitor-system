package com.soap.envmonitorsystem2.service;

import com.soap.envmonitorsystem2.dto.ApiResult;
import com.soap.envmonitorsystem2.dto.SensorLatestResponse;
import com.soap.envmonitorsystem2.dto.SensorType;
import com.soap.envmonitorsystem2.entity.GpsData;
import com.soap.envmonitorsystem2.entity.HumidityData;
import com.soap.envmonitorsystem2.entity.LightData;
import com.soap.envmonitorsystem2.entity.PressureData;
import com.soap.envmonitorsystem2.entity.TemperatureData;
import com.soap.envmonitorsystem2.repository.GpsDataRepository;
import com.soap.envmonitorsystem2.repository.HumidityDataRepository;
import com.soap.envmonitorsystem2.repository.LightDataRepository;
import com.soap.envmonitorsystem2.repository.PressureDataRepository;
import com.soap.envmonitorsystem2.repository.TemperatureDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SensorService {

    private final LightDataRepository lightDataRepository;
    private final TemperatureDataRepository temperatureDataRepository;
    private final HumidityDataRepository humidityDataRepository;
    private final PressureDataRepository pressureDataRepository;
    private final GpsDataRepository gpsDataRepository;

    public SensorService(
            LightDataRepository lightDataRepository,
            TemperatureDataRepository temperatureDataRepository,
            HumidityDataRepository humidityDataRepository,
            PressureDataRepository pressureDataRepository,
            GpsDataRepository gpsDataRepository) {
        this.lightDataRepository = lightDataRepository;
        this.temperatureDataRepository = temperatureDataRepository;
        this.humidityDataRepository = humidityDataRepository;
        this.pressureDataRepository = pressureDataRepository;
        this.gpsDataRepository = gpsDataRepository;
    }

    public SensorLatestResponse latest() {
        var light = lightDataRepository.findTopByOrderByRecordedAtDesc().orElse(null);
        var temp = temperatureDataRepository.findTopByOrderByRecordedAtDesc().orElse(null);
        var hum = humidityDataRepository.findTopByOrderByRecordedAtDesc().orElse(null);
        var press = pressureDataRepository.findTopByOrderByRecordedAtDesc().orElse(null);
        var gps = gpsDataRepository.findTopByOrderByRecordedAtDesc().orElse(null);

        return new SensorLatestResponse(
                light != null ? light.getLux() : null,
                temp != null ? temp.getCelsius() : null,
                hum != null ? hum.getPercent() : null,
                press != null ? press.getHpa() : null,
                gps != null ? gps.getLatitude() : null,
                gps != null ? gps.getLongitude() : null,
                light != null ? format(light.getRecordedAt()) : null,
                temp != null ? format(temp.getRecordedAt()) : null,
                hum != null ? format(hum.getRecordedAt()) : null,
                press != null ? format(press.getRecordedAt()) : null,
                gps != null ? format(gps.getRecordedAt()) : null
        );
    }

    public List<Map<String, Object>> series(SensorType type, int limit) {
        Pageable pageable = PageRequest.of(0, Math.min(limit, 72));
        return switch (type) {
            case LIGHT -> buildChartData(lightDataRepository.findAllByOrderByRecordedAtDesc(pageable), "lux");
            case TEMPERATURE -> buildChartData(temperatureDataRepository.findAllByOrderByRecordedAtDesc(pageable), "celsius");
            case HUMIDITY -> buildChartData(humidityDataRepository.findAllByOrderByRecordedAtDesc(pageable), "percent");
            case PRESSURE -> buildChartData(pressureDataRepository.findAllByOrderByRecordedAtDesc(pageable), "hpa");
        };
    }

    public SensorLatestResponse appendDemoReading() {
        LocalDateTime now = LocalDateTime.now();
        LightData light = lightDataRepository.save(new LightData(round(100 + random() * 900), now));
        TemperatureData temperature = temperatureDataRepository.save(new TemperatureData(round(18 + random() * 10), now));
        HumidityData humidity = humidityDataRepository.save(new HumidityData(round(35 + random() * 50), now));
        PressureData pressure = pressureDataRepository.save(new PressureData(round(970 + random() * 60), now));
        GpsData gps = gpsDataRepository.save(new GpsData(round(30.0 + random() * 0.2, 6), round(120.0 + random() * 0.3, 6), now));
        return new SensorLatestResponse(
                light.getLux(),
                temperature.getCelsius(),
                humidity.getPercent(),
                pressure.getHpa(),
                gps.getLatitude(),
                gps.getLongitude(),
                format(light.getRecordedAt()),
                format(temperature.getRecordedAt()),
                format(humidity.getRecordedAt()),
                format(pressure.getRecordedAt()),
                format(gps.getRecordedAt())
        );
    }

    private List<Map<String, Object>> buildChartData(List<?> rows, String key) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = rows.size() - 1; i >= 0; i--) {
            Object row = rows.get(i);
            Double value = switch (key) {
                case "lux" -> ((LightData) row).getLux();
                case "celsius" -> ((TemperatureData) row).getCelsius();
                case "percent" -> ((HumidityData) row).getPercent();
                case "hpa" -> ((PressureData) row).getHpa();
                default -> null;
            };
            String at = switch (key) {
                case "lux" -> format(((LightData) row).getRecordedAt());
                case "celsius" -> format(((TemperatureData) row).getRecordedAt());
                case "percent" -> format(((HumidityData) row).getRecordedAt());
                case "hpa" -> format(((PressureData) row).getRecordedAt());
                default -> null;
            };
            var entry = new HashMap<String, Object>();
            entry.put("recordedAt", at);
            entry.put(key, value);
            result.add(entry);
        }
        return result;
    }

    private double random() {
        return ThreadLocalRandom.current().nextDouble();
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private double round(double value, int scale) {
        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }

    private String format(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
