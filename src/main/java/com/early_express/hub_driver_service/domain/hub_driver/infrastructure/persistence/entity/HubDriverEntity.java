package com.early_express.hub_driver_service.domain.hub_driver.infrastructure.persistence.entity;

import com.early_express.hub_driver_service.domain.hub_driver.domain.model.HubDriver;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverId;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import com.early_express.hub_driver_service.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * HubDriver JPA Entity
 */
@Entity
@Table(name = "p_hub_driver", indexes = {
        @Index(name = "idx_hub_driver_user_id", columnList = "user_id", unique = true),
        @Index(name = "idx_hub_driver_status", columnList = "status"),
        @Index(name = "idx_hub_driver_priority", columnList = "assignment_priority, available_from")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDriverEntity extends BaseEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "user_id", nullable = false, unique = true, length = 36)
    private String userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HubDriverStatus status;

    @Column(name = "current_delivery_id", length = 36)
    private String currentDeliveryId;

    @Column(name = "assignment_priority", nullable = false)
    private Integer assignmentPriority;

    @Column(name = "total_deliveries", nullable = false)
    private Long totalDeliveries;

    @Column(name = "total_delivery_time_min", nullable = false)
    private Long totalDeliveryTimeMin;

    @Column(name = "average_delivery_time_min", nullable = false)
    private Long averageDeliveryTimeMin;

    @Column(name = "last_delivery_completed_at")
    private LocalDateTime lastDeliveryCompletedAt;

    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Builder
    private HubDriverEntity(String id, String userId, String name,
                            HubDriverStatus status, String currentDeliveryId,
                            Integer assignmentPriority, Long totalDeliveries,
                            Long totalDeliveryTimeMin, Long averageDeliveryTimeMin,
                            LocalDateTime lastDeliveryCompletedAt, LocalDateTime availableFrom) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.status = status;
        this.currentDeliveryId = currentDeliveryId;
        this.assignmentPriority = assignmentPriority;
        this.totalDeliveries = totalDeliveries;
        this.totalDeliveryTimeMin = totalDeliveryTimeMin;
        this.averageDeliveryTimeMin = averageDeliveryTimeMin;
        this.lastDeliveryCompletedAt = lastDeliveryCompletedAt;
        this.availableFrom = availableFrom;
    }

    // ===== 도메인 → 엔티티 변환 =====

    public static HubDriverEntity fromDomain(HubDriver hubDriver) {
        String entityId = hubDriver.getIdValue() != null
                ? hubDriver.getIdValue()
                : UUID.randomUUID().toString();

        return HubDriverEntity.builder()
                .id(entityId)
                .userId(hubDriver.getUserId())
                .name(hubDriver.getName())
                .status(hubDriver.getStatus())
                .currentDeliveryId(hubDriver.getCurrentDeliveryId())
                .assignmentPriority(hubDriver.getAssignmentPriority())
                .totalDeliveries(hubDriver.getTotalDeliveries())
                .totalDeliveryTimeMin(hubDriver.getTotalDeliveryTimeMin())
                .averageDeliveryTimeMin(hubDriver.getAverageDeliveryTimeMin())
                .lastDeliveryCompletedAt(hubDriver.getLastDeliveryCompletedAt())
                .availableFrom(hubDriver.getAvailableFrom())
                .build();
    }

    // ===== 엔티티 → 도메인 변환 =====

    public HubDriver toDomain() {
        return HubDriver.reconstitute(
                HubDriverId.of(this.id),
                this.userId,
                this.name,
                this.status,
                this.currentDeliveryId,
                this.assignmentPriority,
                this.totalDeliveries,
                this.totalDeliveryTimeMin,
                this.averageDeliveryTimeMin,
                this.lastDeliveryCompletedAt,
                this.availableFrom,
                this.getCreatedAt(),
                this.getCreatedBy(),
                this.getUpdatedAt(),
                this.getUpdatedBy(),
                this.getDeletedAt(),
                this.getDeletedBy(),
                this.isDeleted()
        );
    }

    // ===== 도메인 → 엔티티 업데이트 =====

    public void updateFromDomain(HubDriver hubDriver) {
        if (!this.id.equals(hubDriver.getIdValue())) {
            throw new IllegalStateException(
                    "엔티티 ID와 도메인 ID가 일치하지 않습니다. " +
                            "Entity ID: " + this.id + ", Domain ID: " + hubDriver.getIdValue()
            );
        }

        this.name = hubDriver.getName();
        this.status = hubDriver.getStatus();
        this.currentDeliveryId = hubDriver.getCurrentDeliveryId();
        this.assignmentPriority = hubDriver.getAssignmentPriority();
        this.totalDeliveries = hubDriver.getTotalDeliveries();
        this.totalDeliveryTimeMin = hubDriver.getTotalDeliveryTimeMin();
        this.averageDeliveryTimeMin = hubDriver.getAverageDeliveryTimeMin();
        this.lastDeliveryCompletedAt = hubDriver.getLastDeliveryCompletedAt();
        this.availableFrom = hubDriver.getAvailableFrom();
    }
}
