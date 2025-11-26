package com.early_express.hub_driver_service.domain.hub_driver.domain.exception;

import com.early_express.hub_driver_service.global.presentation.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * HubDriver 도메인 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum HubDriverErrorCode implements ErrorCode {

    // 조회 관련 (404)
    HUB_DRIVER_NOT_FOUND("HUB_DRIVER_001", "허브 배송 담당자를 찾을 수 없습니다.", 404),

    // 상태 관련 (400)
    DRIVER_NOT_AVAILABLE("HUB_DRIVER_101", "배정 가능한 상태가 아닙니다.", 400),
    DRIVER_ALREADY_ON_DELIVERY("HUB_DRIVER_102", "이미 배송 중입니다.", 400),
    DRIVER_NOT_ON_DELIVERY("HUB_DRIVER_103", "배송 중이 아닙니다.", 400),

    // 배정 관련 (400)
    NO_AVAILABLE_DRIVER("HUB_DRIVER_201", "배정 가능한 배송 담당자가 없습니다.", 400),
    INVALID_HUB_ID("HUB_DRIVER_202", "유효하지 않은 허브 ID입니다.", 400),

    // 중복 관련 (409)
    HUB_DRIVER_ALREADY_EXISTS("HUB_DRIVER_301", "이미 등록된 배송 담당자입니다.", 409);

    private final String code;
    private final String message;
    private final int status;
}
