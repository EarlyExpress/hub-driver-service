package com.early_express.hub_driver_service.domain.hub_driver.domain.repository;

import com.early_express.hub_driver_service.domain.hub_driver.domain.model.HubDriver;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverId;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * HubDriver Domain Repository Interface
 */
public interface HubDriverRepository {

    HubDriver save(HubDriver hubDriver);

    Optional<HubDriver> findById(HubDriverId id);

    Optional<HubDriver> findByUserId(String userId);

    /**
     * 배정 가능한 드라이버 조회 (전사 단위)
     * 우선순위 낮은 순서 (배정 횟수 적은 순서)
     */
    List<HubDriver> findAvailableDrivers();

    Page<HubDriver> findByStatus(HubDriverStatus status, Pageable pageable);

    Page<HubDriver> findAll(Pageable pageable);

    boolean existsByUserId(String userId);
}
