package com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.repository;

import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverErrorCode;
import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverException;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.HubDriver;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverId;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import com.early_express.hub_driver_service.domain.hub_driver.domain.repository.HubDriverRepository;
import com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.entity.HubDriverEntity;
import com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.entity.QHubDriverEntity;
import com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.jpa.HubDriverJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * HubDriver Repository 구현체
 */
@Repository
@RequiredArgsConstructor
public class HubDriverRepositoryImpl implements HubDriverRepository {

    private final HubDriverJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QHubDriverEntity hubDriver = QHubDriverEntity.hubDriverEntity;

    @Override
    @Transactional
    public HubDriver save(HubDriver hubDriver) {
        HubDriverEntity entity;

        if (hubDriver.getId() != null) {
            entity = jpaRepository.findByIdAndIsDeletedFalse(hubDriver.getIdValue())
                    .orElseThrow(() -> new HubDriverException(
                            HubDriverErrorCode.HUB_DRIVER_NOT_FOUND,
                            "허브 배송 담당자를 찾을 수 없습니다: " + hubDriver.getIdValue()
                    ));
            entity.updateFromDomain(hubDriver);
        } else {
            entity = HubDriverEntity.fromDomain(hubDriver);
            entity = jpaRepository.save(entity);
        }

        return entity.toDomain();
    }

    @Override
    public Optional<HubDriver> findById(HubDriverId id) {
        return jpaRepository.findByIdAndIsDeletedFalse(id.getValue())
                .map(HubDriverEntity::toDomain);
    }

    @Override
    public Optional<HubDriver> findByUserId(String userId) {
        return jpaRepository.findByUserIdAndIsDeletedFalse(userId)
                .map(HubDriverEntity::toDomain);
    }

    @Override
    public List<HubDriver> findAvailableDrivers() {
        List<HubDriverEntity> entities = queryFactory
                .selectFrom(hubDriver)
                .where(
                        statusEq(HubDriverStatus.AVAILABLE),
                        isNotDeleted()
                )
                .orderBy(
                        hubDriver.assignmentPriority.asc(),
                        hubDriver.availableFrom.asc()
                )
                .fetch();

        return entities.stream()
                .map(HubDriverEntity::toDomain)
                .toList();
    }

    @Override
    public Page<HubDriver> findByStatus(HubDriverStatus status, Pageable pageable) {
        return jpaRepository.findByStatusAndIsDeletedFalse(status, pageable)
                .map(HubDriverEntity::toDomain);
    }

    @Override
    public Page<HubDriver> findAll(Pageable pageable) {
        return jpaRepository.findByIsDeletedFalse(pageable)
                .map(HubDriverEntity::toDomain);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return jpaRepository.existsByUserIdAndIsDeletedFalse(userId);
    }

    // ===== BooleanExpression =====

    private BooleanExpression statusEq(HubDriverStatus status) {
        return status != null ? hubDriver.status.eq(status) : null;
    }

    private BooleanExpression isNotDeleted() {
        return hubDriver.isDeleted.eq(false);
    }
}
