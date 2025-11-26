package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.CompleteDeliveryCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송 완료 통지 요청 (Internal)
 * HubDelivery Service → HubDriver Service
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCompleteRequest {

    private Long deliveryTimeMin;

    /**
     * Request → Command 변환
     */
    public CompleteDeliveryCommand toCommand(String driverId) {
        return CompleteDeliveryCommand.builder()
                .driverId(driverId)
                .deliveryTimeMin(this.deliveryTimeMin)
                .build();
    }

    public static DriverCompleteRequest of(Long deliveryTimeMin) {
        return DriverCompleteRequest.builder()
                .deliveryTimeMin(deliveryTimeMin)
                .build();
    }
}
