package com.soap.envmonitorsystem2.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pressure_data")
public class PressureData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double hpa;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public PressureData() {
    }

    public PressureData(Double hpa, LocalDateTime recordedAt) {
        this.hpa = hpa;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Double getHpa() {
        return hpa;
    }

    public void setHpa(Double hpa) {
        this.hpa = hpa;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
