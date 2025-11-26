package com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HubDriver ID 값 객체
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDriverId {

    private String value;

    private HubDriverId(String value) {
        this.value = value;
    }

    public static HubDriverId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("HubDriverId는 null이거나 빈 값일 수 없습니다.");
        }
        return new HubDriverId(value);
    }
}
