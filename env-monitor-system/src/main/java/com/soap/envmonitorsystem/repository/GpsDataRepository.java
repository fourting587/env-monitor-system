package com.soap.envmonitorsystem.repository;

import com.soap.envmonitorsystem.entity.GpsData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GpsDataRepository extends JpaRepository<GpsData, Long> {

    Optional<GpsData> findFirstByOrderByRecordedAtDesc();

    List<GpsData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
