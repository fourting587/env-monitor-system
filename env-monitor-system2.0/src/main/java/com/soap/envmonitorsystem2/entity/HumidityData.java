package com.soap.envmonitorsystem2.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "humidity_data")
public class HumidityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double percent;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public HumidityData() {
    }

    public HumidityData(Double percent, LocalDateTime recordedAt) {
        this.percent = percent;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
