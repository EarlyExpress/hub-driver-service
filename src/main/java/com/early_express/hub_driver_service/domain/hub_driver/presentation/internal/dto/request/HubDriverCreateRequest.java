package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.CreateCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 허브 간 배송 담당자 생성 요청 (Internal)
 * User Service → HubDriver Service
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubDriverCreateRequest {

    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    /**
     * Request → Command 변환
     */
    public CreateCommand toCommand(String createdBy) {
        return CreateCommand.builder()
                .userId(this.userId)
                .name(this.name)
                .createdBy(createdBy)
                .build();
    }

    public static HubDriverCreateRequest of(String userId, String name) {
        return HubDriverCreateRequest.builder()
                .userId(userId)
                .name(name)
                .build();
    }
}
