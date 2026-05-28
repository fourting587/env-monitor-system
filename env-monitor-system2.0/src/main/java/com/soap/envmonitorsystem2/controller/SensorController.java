package com.soap.envmonitorsystem2.controller;

import com.soap.envmonitorsystem2.dto.ApiResult;
import com.soap.envmonitorsystem2.dto.SensorLatestResponse;
import com.soap.envmonitorsystem2.dto.SensorType;
import com.soap.envmonitorsystem2.service.SensorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/latest")
    public ApiResult<SensorLatestResponse> latest() {
        return ApiResult.ok(sensorService.latest());
    }

    @GetMapping("/series/{type}")
    public ApiResult<List<Map<String, Object>>> series(
            @PathVariable SensorType type,
            @RequestParam(defaultValue = "48") int limit) {
        return ApiResult.ok(sensorService.series(type, limit));
    }

    @PostMapping("/demo")
    public ApiResult<SensorLatestResponse> demo() {
        return ApiResult.ok(sensorService.appendDemoReading());
    }
}
