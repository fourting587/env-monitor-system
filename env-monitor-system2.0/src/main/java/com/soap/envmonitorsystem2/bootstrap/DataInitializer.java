package com.soap.envmonitorsystem2.bootstrap;

import com.soap.envmonitorsystem2.entity.GpsData;
import com.soap.envmonitorsystem2.entity.HumidityData;
import com.soap.envmonitorsystem2.entity.LightData;
import com.soap.envmonitorsystem2.entity.PressureData;
import com.soap.envmonitorsystem2.entity.TemperatureData;
import com.soap.envmonitorsystem2.entity.User;
import com.soap.envmonitorsystem2.repository.GpsDataRepository;
import com.soap.envmonitorsystem2.repository.HumidityDataRepository;
import com.soap.envmonitorsystem2.repository.LightDataRepository;
import com.soap.envmonitorsystem2.repository.PressureDataRepository;
import com.soap.envmonitorsystem2.repository.TemperatureDataRepository;
import com.soap.envmonitorsystem2.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final LightDataRepository lightDataRepository;
    private final TemperatureDataRepository temperatureDataRepository;
    private final HumidityDataRepository humidityDataRepository;
    private final PressureDataRepository pressureDataRepository;
    private final GpsDataRepository gpsDataRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepository userRepository,
                           LightDataRepository lightDataRepository,
                           TemperatureDataRepository temperatureDataRepository,
                           HumidityDataRepository humidityDataRepository,
                           PressureDataRepository pressureDataRepository,
                           GpsDataRepository gpsDataRepository) {
        this.userRepository = userRepository;
        this.lightDataRepository = lightDataRepository;
        this.temperatureDataRepository = temperatureDataRepository;
        this.humidityDataRepository = humidityDataRepository;
        this.pressureDataRepository = pressureDataRepository;
        this.gpsDataRepository = gpsDataRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createDefaultUsers();
        createSampleSensorData();
    }

    private void createDefaultUsers() {
        if (userRepository.count() > 0) {
            return;
        }
        userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "系统管理员", "ADMIN", LocalDateTime.now()));
        userRepository.save(new User("demo", passwordEncoder.encode("demo123"), "演示用户", "USER", LocalDateTime.now()));
    }

    private void createSampleSensorData() {
        if (lightDataRepository.count() > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        for (int i = 12; i >= 0; i--) {
            LocalDateTime time = now.minusMinutes(i * 5L);
            double base = 100 + random() * 900;
            lightDataRepository.save(new LightData(round(base), time));
            temperatureDataRepository.save(new TemperatureData(round(18 + random() * 10), time));
            humidityDataRepository.save(new HumidityData(round(35 + random() * 50), time));
            pressureDataRepository.save(new PressureData(round(970 + random() * 60), time));
            gpsDataRepository.save(new GpsData(round(30.0 + random() * 0.2, 6), round(120.0 + random() * 0.3, 6), time));
        }
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
}
