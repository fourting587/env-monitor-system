package com.soap.envmonitorsystem2.bootstrap;

import com.soap.envmonitorsystem2.service.SensorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SensorDataScheduler {

    private final SensorService sensorService;

    public SensorDataScheduler(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Scheduled(initialDelay = 2000, fixedRate = 5000)
    public void generatePeriodicSensorData() {
        sensorService.appendDemoReading();
    }
}
