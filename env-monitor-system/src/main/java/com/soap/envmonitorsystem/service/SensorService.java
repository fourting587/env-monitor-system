package com.soap.envmonitorsystem.service;

import com.soap.envmonitorsystem.dto.SensorLatestResponse;
import com.soap.envmonitorsystem.dto.SensorType;
import com.soap.envmonitorsystem.entity.GpsData;
import com.soap.envmonitorsystem.entity.HumidityData;
import com.soap.envmonitorsystem.entity.LightData;
import com.soap.envmonitorsystem.entity.PressureData;
import com.soap.envmonitorsystem.entity.TemperatureData;
import com.soap.envmonitorsystem.repository.GpsDataRepository;
import com.soap.envmonitorsystem.repository.HumidityDataRepository;
import com.soap.envmonitorsystem.repository.LightDataRepository;
import com.soap.envmonitorsystem.repository.PressureDataRepository;
import com.soap.envmonitorsystem.repository.TemperatureDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final LightDataRepository lightDataRepository;
    private final TemperatureDataRepository temperatureDataRepository;
    private final GpsDataRepository gpsDataRepository;
    private final HumidityDataRepository humidityDataRepository;
    private final PressureDataRepository pressureDataRepository;

    public SensorLatestResponse latest() {
        var l = lightDataRepository.findFirstByOrderByRecordedAtDesc();
        var t = temperatureDataRepository.findFirstByOrderByRecordedAtDesc();
        var g = gpsDataRepository.findFirstByOrderByRecordedAtDesc();
        var h = humidityDataRepository.findFirstByOrderByRecordedAtDesc();
        var p = pressureDataRepository.findFirstByOrderByRecordedAtDesc();

        return new SensorLatestResponse(
                l.map(LightData::getLux).orElse(null),
                l.map(LightData::getRecordedAt).orElse(null),
                t.map(TemperatureData::getCelsius).orElse(null),
                t.map(TemperatureData::getRecordedAt).orElse(null),
                g.map(x -> x.getLatitude().doubleValue()).orElse(null),
                g.map(x -> x.getLongitude().doubleValue()).orElse(null),
                g.map(GpsData::getRecordedAt).orElse(null),
                h.map(HumidityData::getPercent).orElse(null),
                h.map(HumidityData::getRecordedAt).orElse(null),
                p.map(PressureData::getHpa).orElse(null),
                p.map(PressureData::getRecordedAt).orElse(null)
        );
    }

    public List<Map<String, Object>> series(SensorType type, int limit) {
        int cap = Math.min(Math.max(limit, 1), 500);
        var pageable = PageRequest.of(0, cap);
        List<Map<String, Object>> rows = new ArrayList<>();
        switch (type) {
            case light -> {
                for (LightData d : lightDataRepository.findAllByOrderByRecordedAtDesc(pageable)) {
                    rows.add(point(d.getRecordedAt(), "lux", d.getLux()));
                }
            }
            case temperature -> {
                for (TemperatureData d : temperatureDataRepository.findAllByOrderByRecordedAtDesc(pageable)) {
                    rows.add(point(d.getRecordedAt(), "celsius", d.getCelsius()));
                }
            }
            case gps -> {
                for (GpsData d : gpsDataRepository.findAllByOrderByRecordedAtDesc(pageable)) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("recordedAt", d.getRecordedAt());
                    m.put("latitude", d.getLatitude().doubleValue());
                    m.put("longitude", d.getLongitude().doubleValue());
                    rows.add(m);
                }
            }
            case humidity -> {
                for (HumidityData d : humidityDataRepository.findAllByOrderByRecordedAtDesc(pageable)) {
                    rows.add(point(d.getRecordedAt(), "percent", d.getPercent()));
                }
            }
            case pressure -> {
                for (PressureData d : pressureDataRepository.findAllByOrderByRecordedAtDesc(pageable)) {
                    rows.add(point(d.getRecordedAt(), "hpa", d.getHpa()));
                }
            }
        }
        Collections.reverse(rows);
        return rows;
    }

    private Map<String, Object> point(LocalDateTime at, String key, double value) {
        Map<String, Object> m = new HashMap<>();
        m.put("recordedAt", at);
        m.put(key, value);
        return m;
    }

    /** 生成一组随机演示数据（实训演示用） */
    @Transactional
    public SensorLatestResponse appendDemoReading() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        LocalDateTime now = LocalDateTime.now();

        LightData ld = new LightData();
        ld.setLux(200 + r.nextDouble(500));
        ld.setRecordedAt(now);
        lightDataRepository.save(ld);

        TemperatureData td = new TemperatureData();
        td.setCelsius(round2(18 + r.nextDouble(15)));
        td.setRecordedAt(now);
        temperatureDataRepository.save(td);

        GpsData gd = new GpsData();
        gd.setLatitude(BigDecimal.valueOf(39.9 + r.nextDouble(0.2)).setScale(7, RoundingMode.HALF_UP));
        gd.setLongitude(BigDecimal.valueOf(116.3 + r.nextDouble(0.2)).setScale(7, RoundingMode.HALF_UP));
        gd.setRecordedAt(now);
        gpsDataRepository.save(gd);

        HumidityData hd = new HumidityData();
        hd.setPercent(round2(40 + r.nextDouble(40)));
        hd.setRecordedAt(now);
        humidityDataRepository.save(hd);

        PressureData pd = new PressureData();
        pd.setHpa(round2(1000 + r.nextDouble(30)));
        pd.setRecordedAt(now);
        pressureDataRepository.save(pd);

        return latest();
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
