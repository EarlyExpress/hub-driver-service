package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.AssignDeliveryCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 드라이버 자동 배정 요청 (Internal)
 * HubDelivery Service → HubDriver Service
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverAssignRequest {

    @NotBlank(message = "허브 배송 ID는 필수입니다.")
    private String hubDeliveryId;

    /**
     * Request → Command 변환
     */
    public AssignDeliveryCommand toCommand() {
        return AssignDeliveryCommand.builder()
                .hubDeliveryId(this.hubDeliveryId)
                .build();
    }

    public static DriverAssignRequest of(String hubDeliveryId) {
        return DriverAssignRequest.builder()
                .hubDeliveryId(hubDeliveryId)
                .build();
    }
}
