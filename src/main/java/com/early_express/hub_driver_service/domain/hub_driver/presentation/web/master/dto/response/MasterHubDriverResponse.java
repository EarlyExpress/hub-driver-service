package com.early_express.hub_driver_service.domain.hub_driver.presentation.web.master.dto.response;

import com.early_express.hub_driver_service.domain.hub_driver.application.query.dto.HubDriverQueryDto.HubDriverResponse;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 마스터용 드라이버 간단 응답
 */
@Getter
@Builder
public class MasterHubDriverResponse {

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
    private LocalDateTime createdAt;

    /**
     * Query DTO → Presentation DTO 변환
     */
    public static MasterHubDriverResponse from(HubDriverResponse driver) {
        return MasterHubDriverResponse.builder()
                .driverId(driver.getDriverId())
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
                .createdAt(driver.getCreatedAt())
                .build();
    }
}
