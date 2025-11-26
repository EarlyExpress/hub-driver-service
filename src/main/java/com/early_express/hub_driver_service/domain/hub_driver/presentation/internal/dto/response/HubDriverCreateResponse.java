package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.response;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.CreateResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 허브 간 배송 담당자 생성 응답 (Internal)
 * HubDriver Service → User Service
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubDriverCreateResponse {

    private String driverId;
    private String userId;
    private String name;
    private String status;

    /**
     * Command Result → Response 변환
     */
    public static HubDriverCreateResponse from(CreateResult result) {
        return HubDriverCreateResponse.builder()
                .driverId(result.getDriverId())
                .userId(result.getUserId())
                .name(result.getName())
                .status(result.getStatus())
                .build();
    }
}