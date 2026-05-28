package com.soap.envmonitorsystem2.repository;

import com.soap.envmonitorsystem2.entity.PressureData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PressureDataRepository extends JpaRepository<PressureData, Long> {
    Optional<PressureData> findTopByOrderByRecordedAtDesc();
    List<PressureData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
