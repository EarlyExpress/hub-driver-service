package com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.jpa;

import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.entity.HubDriverEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * HubDriver JPA Repository
 */
public interface HubDriverJpaRepository extends JpaRepository<HubDriverEntity, String> {

    Optional<HubDriverEntity> findByIdAndIsDeletedFalse(String id);

    Optional<HubDriverEntity> findByUserIdAndIsDeletedFalse(String userId);

    boolean existsByUserIdAndIsDeletedFalse(String userId);

    Page<HubDriverEntity> findByStatusAndIsDeletedFalse(HubDriverStatus status, Pageable pageable);

    Page<HubDriverEntity> findByIsDeletedFalse(Pageable pageable);
}
