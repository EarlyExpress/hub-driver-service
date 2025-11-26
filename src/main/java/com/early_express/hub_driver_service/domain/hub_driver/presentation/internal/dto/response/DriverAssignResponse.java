package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.response;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.DriverAssignResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 드라이버 배정 응답 (Internal)
 * HubDriver Service → HubDelivery Service
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverAssignResponse {

    private String driverId;
    private String userId;
    private String driverName;
    private String status;
    private LocalDateTime assignedAt;

    /**
     * Command Result → Response 변환
     */
    public static DriverAssignResponse from(DriverAssignResult result) {
        return DriverAssignResponse.builder()
                .driverId(result.getDriverId())
                .userId(result.getUserId())
                .driverName(result.getDriverName())
                .status(result.getStatus())
                .assignedAt(result.getAssignedAt())
                .build();
    }
}
