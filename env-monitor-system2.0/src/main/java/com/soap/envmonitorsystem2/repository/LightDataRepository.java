package com.soap.envmonitorsystem2.repository;

import com.soap.envmonitorsystem2.entity.LightData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LightDataRepository extends JpaRepository<LightData, Long> {
    Optional<LightData> findTopByOrderByRecordedAtDesc();
    List<LightData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
