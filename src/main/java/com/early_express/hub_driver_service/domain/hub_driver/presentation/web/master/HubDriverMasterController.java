package com.early_express.hub_driver_service.domain.hub_driver.presentation.web.master;

import com.early_express.hub_driver_service.domain.hub_driver.application.query.HubDriverQueryService;
import com.early_express.hub_driver_service.domain.hub_driver.application.query.dto.HubDriverQueryDto.HubDriverDetailResponse;
import com.early_express.hub_driver_service.domain.hub_driver.application.query.dto.HubDriverQueryDto.HubDriverResponse;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverStatus;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.web.master.dto.response.MasterHubDriverDetailResponse;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.web.master.dto.response.MasterHubDriverResponse;
import com.early_express.hub_driver_service.global.common.dto.PageInfo;
import com.early_express.hub_driver_service.global.presentation.dto.ApiResponse;
import com.early_express.hub_driver_service.global.presentation.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Master HubDriver Controller
 * 마스터 관리자용 드라이버 조회 API
 */
@Slf4j
@RestController
@RequestMapping("/v1/hub-driver/web/master")
@RequiredArgsConstructor
public class HubDriverMasterController {

    private final HubDriverQueryService queryService;

    /**
     * 드라이버 목록 조회
     * GET /v1/hub-driver/web/master/drivers
     */
    @GetMapping("/drivers")
    public ApiResponse<PageResponse<MasterHubDriverResponse>> getDrivers(
            @RequestParam(required = false) HubDriverStatus status,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Roles") String roles,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        log.info("마스터 드라이버 목록 조회 - status: {}", status);

        // TODO: roles 검증 (MASTER 권한 확인)

        Page<HubDriverResponse> queryResult = status != null
                ? queryService.findByStatus(status, pageable)
                : queryService.findAll(pageable);

        List<MasterHubDriverResponse> content = queryResult.getContent().stream()
                .map(MasterHubDriverResponse::from)
                .toList();

        return ApiResponse.success(PageResponse.of(content, PageInfo.of(queryResult)));
    }

    /**
     * 드라이버 상세 조회
     * GET /v1/hub-driver/web/master/drivers/{driverId}
     */
    @GetMapping("/drivers/{driverId}")
    public ApiResponse<MasterHubDriverDetailResponse> getDriverDetail(
            @PathVariable String driverId,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Roles") String roles) {

        log.info("마스터 드라이버 상세 조회 - driverId: {}", driverId);

        // TODO: roles 검증 (MASTER 권한 확인)

        HubDriverDetailResponse queryResult = queryService.findById(driverId);
        MasterHubDriverDetailResponse response = MasterHubDriverDetailResponse.from(queryResult);

        return ApiResponse.success(response);
    }
}
