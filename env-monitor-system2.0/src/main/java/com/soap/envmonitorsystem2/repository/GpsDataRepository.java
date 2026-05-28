package com.soap.envmonitorsystem2.repository;

import com.soap.envmonitorsystem2.entity.GpsData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GpsDataRepository extends JpaRepository<GpsData, Long> {
    Optional<GpsData> findTopByOrderByRecordedAtDesc();
    List<GpsData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
