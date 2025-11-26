package com.early_express.hub_driver_service.domain.hub_driver.presentation.internal;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.HubDriverCommandService;
import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.*;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request.DriverAssignRequest;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request.DriverCancelRequest;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request.DriverCompleteRequest;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.request.HubDriverCreateRequest;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.response.DriverAssignResponse;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.response.DriverOperationResponse;
import com.early_express.hub_driver_service.domain.hub_driver.presentation.internal.dto.response.HubDriverCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * HubDriver Internal Controller
 * 내부 서비스 간 통신용 (HubDelivery Service)
 */
@Slf4j
@RestController
@RequestMapping("/v1/hub-driver/internal")
@RequiredArgsConstructor
public class HubDriverInternalController {

    private final HubDriverCommandService hubDriverCommandService;

    /**
     * 허브 간 배송 담당자 생성
     * POST /v1/hub-driver/internal/drivers
     *
     * User Service에서 허브 간 배송 담당자 등록 시 호출
     */
    @PostMapping("/drivers")
    public HubDriverCreateResponse createDriver(
            @Valid @RequestBody HubDriverCreateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        log.info("[Internal] 허브 간 배송 담당자 생성 요청 - userId: {}, name: {}",
                request.getUserId(), request.getName());

        CreateCommand command = request.toCommand(userId);
        CreateResult result = hubDriverCommandService.create(command);

        log.info("[Internal] 허브 간 배송 담당자 생성 완료 - driverId: {}, userId: {}",
                result.getDriverId(), result.getUserId());

        return HubDriverCreateResponse.from(result);
    }

    /**
     * 드라이버 자동 배정
     * POST /v1/hub-driver/internal/drivers/assign
     *
     * HubDelivery 생성 시 호출
     * 배정 가능한 드라이버 중 우선순위가 가장 낮은 드라이버에게 자동 배정
     */
    @PostMapping("/drivers/assign")
    public DriverAssignResponse assignDriver(@Valid @RequestBody DriverAssignRequest request) {
        log.info("[Internal] 드라이버 자동 배정 요청 - hubDeliveryId: {}", request.getHubDeliveryId());

        AssignDeliveryCommand command = request.toCommand();
        DriverAssignResult result = hubDriverCommandService.assignDelivery(command);

        log.info("[Internal] 드라이버 배정 완료 - driverId: {}, hubDeliveryId: {}",
                result.getDriverId(), request.getHubDeliveryId());

        return DriverAssignResponse.from(result);
    }

    /**
     * 배송 완료 통지
     * PUT /v1/hub-driver/internal/drivers/{driverId}/complete
     *
     * HubDelivery 완료 시 호출
     * 드라이버 상태를 AVAILABLE로 변경하고 통계 업데이트
     */
    @PutMapping("/drivers/{driverId}/complete")
    public DriverOperationResponse completeDelivery(
            @PathVariable String driverId,
            @RequestBody DriverCompleteRequest request) {

        log.info("[Internal] 배송 완료 통지 - driverId: {}, deliveryTime: {}분",
                driverId, request.getDeliveryTimeMin());

        CompleteDeliveryCommand command = request.toCommand(driverId);
        hubDriverCommandService.completeDelivery(command);

        log.info("[Internal] 배송 완료 처리 완료 - driverId: {}", driverId);

        return DriverOperationResponse.completed(driverId);
    }

    /**
     * 배송 취소 통지
     * PUT /v1/hub-driver/internal/drivers/{driverId}/cancel
     *
     * HubDelivery 취소 시 호출
     * 드라이버 배정 해제 및 상태를 AVAILABLE로 변경
     */
    @PutMapping("/drivers/{driverId}/cancel")
    public DriverOperationResponse cancelDelivery(
            @PathVariable String driverId,
            @RequestBody(required = false) DriverCancelRequest request) {

        log.info("[Internal] 배송 취소 통지 - driverId: {}", driverId);

        CancelDeliveryCommand command = CancelDeliveryCommand.builder()
                .driverId(driverId)
                .build();

        hubDriverCommandService.cancelDelivery(command);

        log.info("[Internal] 배송 취소 처리 완료 - driverId: {}", driverId);

        return DriverOperationResponse.cancelled(driverId);
    }
}

