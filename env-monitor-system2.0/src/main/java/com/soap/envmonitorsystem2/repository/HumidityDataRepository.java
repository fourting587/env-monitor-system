package com.soap.envmonitorsystem2.repository;

import com.soap.envmonitorsystem2.entity.HumidityData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HumidityDataRepository extends JpaRepository<HumidityData, Long> {
    Optional<HumidityData> findTopByOrderByRecordedAtDesc();
    List<HumidityData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
