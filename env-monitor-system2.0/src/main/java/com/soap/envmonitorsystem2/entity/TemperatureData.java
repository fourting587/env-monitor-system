package com.soap.envmonitorsystem2.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "temperature_data")
public class TemperatureData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double celsius;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public TemperatureData() {
    }

    public TemperatureData(Double celsius, LocalDateTime recordedAt) {
        this.celsius = celsius;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Double getCelsius() {
        return celsius;
    }

    public void setCelsius(Double celsius) {
        this.celsius = celsius;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
