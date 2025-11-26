package com.early_express.hub_driver_service.domain.hub_driver.domain.model;

import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverErrorCode;
import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverException;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverId;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * HubDriver Aggregate Root
 * 허브 간 배송 담당자 (전사 소속)
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDriver {

    private HubDriverId id;
    private String userId;
    private String name;
    private HubDriverStatus status;
    private String currentDeliveryId;
    private Integer assignmentPriority;
    private Long totalDeliveries;
    private Long totalDeliveryTimeMin;
    private Long averageDeliveryTimeMin;
    private LocalDateTime lastDeliveryCompletedAt;
    private LocalDateTime availableFrom;

    // Audit 필드
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private boolean isDeleted;

    @Builder
    private HubDriver(HubDriverId id, String userId, String name,
                      HubDriverStatus status, String currentDeliveryId,
                      Integer assignmentPriority, Long totalDeliveries,
                      Long totalDeliveryTimeMin, Long averageDeliveryTimeMin,
                      LocalDateTime lastDeliveryCompletedAt, LocalDateTime availableFrom,
                      LocalDateTime createdAt, String createdBy,
                      LocalDateTime updatedAt, String updatedBy,
                      LocalDateTime deletedAt, String deletedBy, boolean isDeleted) {
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
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.isDeleted = isDeleted;
    }

    // ===== 팩토리 메서드 =====

    /**
     * 새로운 HubDriver 생성
     */
    public static HubDriver create(String userId, String name, String createdBy) {
        validateNotBlank(userId, "사용자 ID");
        validateNotBlank(name, "이름");

        return HubDriver.builder()
                .id(null)  // Entity에서 UUID 생성
                .userId(userId)
                .name(name)
                .status(HubDriverStatus.AVAILABLE)
                .assignmentPriority(0)
                .totalDeliveries(0L)
                .totalDeliveryTimeMin(0L)
                .averageDeliveryTimeMin(0L)
                .availableFrom(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .isDeleted(false)
                .build();
    }

    /**
     * DB 조회 후 도메인 복원용
     */
    public static HubDriver reconstitute(
            HubDriverId id, String userId, String name,
            HubDriverStatus status, String currentDeliveryId,
            Integer assignmentPriority, Long totalDeliveries,
            Long totalDeliveryTimeMin, Long averageDeliveryTimeMin,
            LocalDateTime lastDeliveryCompletedAt, LocalDateTime availableFrom,
            LocalDateTime createdAt, String createdBy,
            LocalDateTime updatedAt, String updatedBy,
            LocalDateTime deletedAt, String deletedBy, boolean isDeleted) {

        return HubDriver.builder()
                .id(id)
                .userId(userId)
                .name(name)
                .status(status)
                .currentDeliveryId(currentDeliveryId)
                .assignmentPriority(assignmentPriority)
                .totalDeliveries(totalDeliveries)
                .totalDeliveryTimeMin(totalDeliveryTimeMin)
                .averageDeliveryTimeMin(averageDeliveryTimeMin)
                .lastDeliveryCompletedAt(lastDeliveryCompletedAt)
                .availableFrom(availableFrom)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .updatedAt(updatedAt)
                .updatedBy(updatedBy)
                .deletedAt(deletedAt)
                .deletedBy(deletedBy)
                .isDeleted(isDeleted)
                .build();
    }

    // ===== 비즈니스 메서드 =====

    /**
     * 배송 배정
     */
    public void assignDelivery(String deliveryId) {
        if (!this.status.canAssign()) {
            throw new HubDriverException(
                    HubDriverErrorCode.DRIVER_NOT_AVAILABLE,
                    String.format("배송 담당자가 배정 가능한 상태가 아닙니다. 현재 상태: %s",
                            this.status.getDescription())
            );
        }

        this.currentDeliveryId = deliveryId;
        this.status = HubDriverStatus.ON_DELIVERY;
        this.assignmentPriority++;  // 배정될 때마다 우선순위 증가

        log.info("배송 배정 - driverId: {}, deliveryId: {}", this.getIdValue(), deliveryId);
    }

    /**
     * 배송 완료
     */
    public void completeDelivery(Long deliveryTimeMin) {
        if (this.status != HubDriverStatus.ON_DELIVERY) {
            throw new HubDriverException(
                    HubDriverErrorCode.DRIVER_NOT_ON_DELIVERY,
                    "배송 중이 아닌 담당자는 배송을 완료할 수 없습니다."
            );
        }

        // 통계 업데이트
        this.totalDeliveries++;
        if (deliveryTimeMin != null) {
            this.totalDeliveryTimeMin += deliveryTimeMin;
            this.averageDeliveryTimeMin = this.totalDeliveryTimeMin / this.totalDeliveries;
        }

        this.lastDeliveryCompletedAt = LocalDateTime.now();
        this.currentDeliveryId = null;
        this.status = HubDriverStatus.AVAILABLE;
        this.availableFrom = LocalDateTime.now();
        this.assignmentPriority = 0;  // 완료 후 우선순위 초기화

        log.info("배송 완료 - driverId: {}, totalDeliveries: {}, avgTime: {}분",
                this.getIdValue(), this.totalDeliveries, this.averageDeliveryTimeMin);
    }

    /**
     * 배송 취소 (배정 해제)
     */
    public void cancelDelivery() {
        if (this.status != HubDriverStatus.ON_DELIVERY) {
            throw new HubDriverException(
                    HubDriverErrorCode.DRIVER_NOT_ON_DELIVERY,
                    "배송 중이 아닌 담당자는 배송을 취소할 수 없습니다."
            );
        }

        this.currentDeliveryId = null;
        this.status = HubDriverStatus.AVAILABLE;
        this.availableFrom = LocalDateTime.now();

        log.info("배송 취소 - driverId: {}", this.getIdValue());
    }

    /**
     * 근무 시작
     */
    public void startWork() {
        if (this.status == HubDriverStatus.OFF_DUTY) {
            this.status = HubDriverStatus.AVAILABLE;
            this.availableFrom = LocalDateTime.now();
            log.info("근무 시작 - driverId: {}", this.getIdValue());
        }
    }

    /**
     * 근무 종료
     */
    public void endWork() {
        if (this.status == HubDriverStatus.ON_DELIVERY) {
            throw new HubDriverException(
                    HubDriverErrorCode.DRIVER_ALREADY_ON_DELIVERY,
                    "배송 중인 담당자는 근무를 종료할 수 없습니다."
            );
        }
        this.status = HubDriverStatus.OFF_DUTY;
        log.info("근무 종료 - driverId: {}", this.getIdValue());
    }

    /**
     * 휴직 처리
     */
    public void deactivate() {
        if (this.status == HubDriverStatus.ON_DELIVERY) {
            throw new HubDriverException(
                    HubDriverErrorCode.DRIVER_ALREADY_ON_DELIVERY,
                    "배송 중인 담당자는 휴직 처리할 수 없습니다."
            );
        }
        this.status = HubDriverStatus.INACTIVE;
        log.info("휴직 처리 - driverId: {}", this.getIdValue());
    }

    /**
     * 복직 처리
     */
    public void activate() {
        if (this.status == HubDriverStatus.INACTIVE) {
            this.status = HubDriverStatus.AVAILABLE;
            this.availableFrom = LocalDateTime.now();
            log.info("복직 처리 - driverId: {}", this.getIdValue());
        }
    }

    // ===== Soft Delete =====

    public void delete(String deletedBy) {
        if (this.status == HubDriverStatus.ON_DELIVERY) {
            throw new HubDriverException(
                    HubDriverErrorCode.DRIVER_ALREADY_ON_DELIVERY,
                    "배송 중인 담당자는 삭제할 수 없습니다."
            );
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    // ===== 검증 메서드 =====

    private static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new HubDriverException(
                    HubDriverErrorCode.INVALID_HUB_ID,
                    fieldName + "는 필수입니다."
            );
        }
    }

    // ===== 조회 메서드 =====

    public String getIdValue() {
        return this.id != null ? this.id.getValue() : null;
    }

    public boolean isAvailable() {
        return this.status == HubDriverStatus.AVAILABLE;
    }

    public boolean isOnDelivery() {
        return this.status == HubDriverStatus.ON_DELIVERY;
    }

    public boolean canWork() {
        return this.status.isWorking();
    }
}