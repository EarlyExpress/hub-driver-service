package com.early_express.hub_driver_service.domain.hub_driver.application.query.dto;

import com.early_express.hub_driver_service.domain.hub_driver.domain.model.HubDriver;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * HubDriver Query DTO
 */
public class HubDriverQueryDto {

    /**
     * 드라이버 조회 응답
     */
    @Getter
    @Builder
    public static class HubDriverResponse {
        private String driverId;
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
        private LocalDateTime createdAt;

        public static HubDriverResponse from(HubDriver driver) {
            return HubDriverResponse.builder()
                    .driverId(driver.getIdValue())
                    .userId(driver.getUserId())
                    .name(driver.getName())
                    .status(driver.getStatus())
                    .currentDeliveryId(driver.getCurrentDeliveryId())
                    .assignmentPriority(driver.getAssignmentPriority())
                    .totalDeliveries(driver.getTotalDeliveries())
                    .totalDeliveryTimeMin(driver.getTotalDeliveryTimeMin())
                    .averageDeliveryTimeMin(driver.getAverageDeliveryTimeMin())
                    .lastDeliveryCompletedAt(driver.getLastDeliveryCompletedAt())
                    .availableFrom(driver.getAvailableFrom())
                    .createdAt(driver.getCreatedAt())
                    .build();
        }
    }

    /**
     * 드라이버 상세 응답
     */
    @Getter
    @Builder
    public static class HubDriverDetailResponse {
        private String driverId;
        private String userId;
        private String name;
        private HubDriverStatus status;
        private String statusDescription;
        private String currentDeliveryId;
        private Integer assignmentPriority;
        private Long totalDeliveries;
        private Long totalDeliveryTimeMin;
        private Long averageDeliveryTimeMin;
        private LocalDateTime lastDeliveryCompletedAt;
        private LocalDateTime availableFrom;
        private Boolean isAvailable;
        private Boolean isOnDelivery;
        private LocalDateTime createdAt;
        private String createdBy;

        public static HubDriverDetailResponse from(HubDriver driver) {
            return HubDriverDetailResponse.builder()
                    .driverId(driver.getIdValue())
                    .userId(driver.getUserId())
                    .name(driver.getName())
                    .status(driver.getStatus())
                    .statusDescription(driver.getStatus().getDescription())
                    .currentDeliveryId(driver.getCurrentDeliveryId())
                    .assignmentPriority(driver.getAssignmentPriority())
                    .totalDeliveries(driver.getTotalDeliveries())
                    .totalDeliveryTimeMin(driver.getTotalDeliveryTimeMin())
                    .averageDeliveryTimeMin(driver.getAverageDeliveryTimeMin())
                    .lastDeliveryCompletedAt(driver.getLastDeliveryCompletedAt())
                    .availableFrom(driver.getAvailableFrom())
                    .isAvailable(driver.isAvailable())
                    .isOnDelivery(driver.isOnDelivery())
                    .createdAt(driver.getCreatedAt())
                    .createdBy(driver.getCreatedBy())
                    .build();
        }
    }
}
