package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.CancelDeliveryCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송 취소 통지 요청 (Internal)
 * HubDelivery Service → HubDriver Service
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCancelRequest {

    private String reason;

    /**
     * Request → Command 변환
     */
    public CancelDeliveryCommand toCommand(String driverId) {
        return CancelDeliveryCommand.builder()
                .driverId(driverId)
                .build();
    }

    public static DriverCancelRequest of(String reason) {
        return DriverCancelRequest.builder()
                .reason(reason)
                .build();
    }
}
