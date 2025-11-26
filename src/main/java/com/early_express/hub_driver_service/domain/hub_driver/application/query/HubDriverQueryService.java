package com.early_express.hub_driver_service.domain.hub_driver.application.query;

import com.early_express.hub_driver_service.domain.hub_driver.application.query.dto.HubDriverQueryDto.*;
import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverErrorCode;
import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverException;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.HubDriver;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverId;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import com.early_express.hub_driver_service.domain.hub_driver.domain.repository.HubDriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * HubDriver Query Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubDriverQueryService {

    private final HubDriverRepository hubDriverRepository;

    /**
     * ID로 상세 조회
     */
    public HubDriverDetailResponse findById(String driverId) {
        HubDriver driver = hubDriverRepository.findById(HubDriverId.of(driverId))
                .orElseThrow(() -> new HubDriverException(
                        HubDriverErrorCode.HUB_DRIVER_NOT_FOUND,
                        "허브 배송 담당자를 찾을 수 없습니다: " + driverId
                ));

        return HubDriverDetailResponse.from(driver);
    }

    /**
     * 사용자 ID로 조회
     */
    public HubDriverDetailResponse findByUserId(String userId) {
        HubDriver driver = hubDriverRepository.findByUserId(userId)
                .orElseThrow(() -> new HubDriverException(
                        HubDriverErrorCode.HUB_DRIVER_NOT_FOUND,
                        "해당 사용자의 배송 담당자 정보를 찾을 수 없습니다: " + userId
                ));

        return HubDriverDetailResponse.from(driver);
    }

    /**
     * 전체 목록 조회
     */
    public Page<HubDriverResponse> findAll(Pageable pageable) {
        return hubDriverRepository.findAll(pageable)
                .map(HubDriverResponse::from);
    }

    /**
     * 상태별 목록 조회
     */
    public Page<HubDriverResponse> findByStatus(HubDriverStatus status, Pageable pageable) {
        return hubDriverRepository.findByStatus(status, pageable)
                .map(HubDriverResponse::from);
    }
}

