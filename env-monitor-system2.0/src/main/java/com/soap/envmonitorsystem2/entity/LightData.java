package com.soap.envmonitorsystem2.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "light_data")
public class LightData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double lux;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public LightData() {
    }

    public LightData(Double lux, LocalDateTime recordedAt) {
        this.lux = lux;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Double getLux() {
        return lux;
    }

    public void setLux(Double lux) {
        this.lux = lux;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
