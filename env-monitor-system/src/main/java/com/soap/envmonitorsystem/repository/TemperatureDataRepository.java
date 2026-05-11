package com.soap.envmonitorsystem.repository;

import com.soap.envmonitorsystem.entity.TemperatureData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemperatureDataRepository extends JpaRepository<TemperatureData, Long> {

    Optional<TemperatureData> findFirstByOrderByRecordedAtDesc();

    List<TemperatureData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
