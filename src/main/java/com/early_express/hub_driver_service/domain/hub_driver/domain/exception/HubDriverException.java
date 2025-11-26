package com.early_express.hub_driver_service.domain.hub_driver.domain.exception;

import com.early_express.hub_driver_service.global.presentation.exception.GlobalException;

/**
 * HubDriver 도메인 예외
 */
public class HubDriverException extends GlobalException {

    public HubDriverException(HubDriverErrorCode errorCode) {
        super(errorCode);
    }

    public HubDriverException(HubDriverErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public HubDriverException(HubDriverErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public HubDriverException(HubDriverErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}