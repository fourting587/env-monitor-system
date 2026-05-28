package com.soap.envmonitorsystem2.repository;

import com.soap.envmonitorsystem2.entity.TemperatureData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemperatureDataRepository extends JpaRepository<TemperatureData, Long> {
    Optional<TemperatureData> findTopByOrderByRecordedAtDesc();
    List<TemperatureData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
