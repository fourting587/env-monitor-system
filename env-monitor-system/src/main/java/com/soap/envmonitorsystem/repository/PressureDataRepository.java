package com.soap.envmonitorsystem.repository;

import com.soap.envmonitorsystem.entity.PressureData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PressureDataRepository extends JpaRepository<PressureData, Long> {

    Optional<PressureData> findFirstByOrderByRecordedAtDesc();

    List<PressureData> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
