package com.early_express.hub_driver_service.domain.hub_driver.application.command;

import com.early_express.hub_driver_service.domain.hub_driver.application.command.dto.HubDriverCommandDto.*;
import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverErrorCode;
import com.early_express.hub_driver_service.domain.hub_driver.domain.exception.HubDriverException;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.HubDriver;
import com.early_express.hub_driver_service.domain.hub_driver.domain.model.vo.HubDriverId;
import com.early_express.hub_driver_service.domain.hub_driver.domain.repository.HubDriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * HubDriver Command Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HubDriverCommandService {

    private final HubDriverRepository hubDriverRepository;

    /**
     * 드라이버 생성
     */
    public CreateResult create(CreateCommand command) {
        log.info("드라이버 생성 - userId: {}, name: {}", command.getUserId(), command.getName());

        // 중복 체크
        if (hubDriverRepository.existsByUserId(command.getUserId())) {
            throw new HubDriverException(
                    HubDriverErrorCode.HUB_DRIVER_ALREADY_EXISTS,
                    "이미 등록된 배송 담당자입니다: " + command.getUserId()
            );
        }

        // 드라이버 생성
        HubDriver driver = HubDriver.create(
                command.getUserId(),
                command.getName(),
                command.getCreatedBy()
        );

        // 저장
        HubDriver savedDriver = hubDriverRepository.save(driver);

        log.info("드라이버 생성 완료 - driverId: {}, userId: {}",
                savedDriver.getIdValue(), savedDriver.getUserId());

        return CreateResult.success(
                savedDriver.getIdValue(),
                savedDriver.getUserId(),
                savedDriver.getName(),
                savedDriver.getStatus().name()
        );
    }

    /**
     * 배송 자동 배정 (가장 우선순위 낮은 드라이버)
     */
    public DriverAssignResult assignDelivery(AssignDeliveryCommand command) {
        log.info("배송 자동 배정 시작 - hubDeliveryId: {}", command.getHubDeliveryId());

        // 배정 가능한 드라이버 조회 (priority 낮은 순)
        List<HubDriver> availableDrivers = hubDriverRepository.findAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            throw new HubDriverException(
                    HubDriverErrorCode.NO_AVAILABLE_DRIVER,
                    "배정 가능한 배송 담당자가 없습니다."
            );
        }

        // 첫 번째 드라이버 배정
        HubDriver driver = availableDrivers.get(0);
        driver.assignDelivery(command.getHubDeliveryId());

        // 저장
        hubDriverRepository.save(driver);

        log.info("배송 배정 완료 - driverId: {}, hubDeliveryId: {}, priority: {}",
                driver.getIdValue(), command.getHubDeliveryId(), driver.getAssignmentPriority());

        return DriverAssignResult.success(
                driver.getIdValue(),
                driver.getUserId(),
                driver.getName(),
                driver.getStatus().name()
        );
    }

    /**
     * 배송 완료 처리
     */
    public void completeDelivery(CompleteDeliveryCommand command) {
        log.info("배송 완료 처리 - driverId: {}, deliveryTime: {}분",
                command.getDriverId(), command.getDeliveryTimeMin());

        HubDriver driver = findDriver(command.getDriverId());

        // 배송 완료
        driver.completeDelivery(command.getDeliveryTimeMin());

        // 저장
        hubDriverRepository.save(driver);

        log.info("배송 완료 - driverId: {}, totalDeliveries: {}, avgTime: {}분",
                driver.getIdValue(), driver.getTotalDeliveries(), driver.getAverageDeliveryTimeMin());
    }

    /**
     * 배송 취소 (배정 해제)
     */
    public void cancelDelivery(CancelDeliveryCommand command) {
        log.info("배송 취소 처리 - driverId: {}", command.getDriverId());

        HubDriver driver = findDriver(command.getDriverId());

        // 배송 취소
        driver.cancelDelivery();

        // 저장
        hubDriverRepository.save(driver);

        log.info("배송 취소 완료 - driverId: {}", driver.getIdValue());
    }

    /**
     * 근무 시작
     */
    public void startWork(StartWorkCommand command) {
        log.info("근무 시작 - driverId: {}", command.getDriverId());

        HubDriver driver = findDriver(command.getDriverId());

        driver.startWork();

        hubDriverRepository.save(driver);

        log.info("근무 시작 완료 - driverId: {}", driver.getIdValue());
    }

    /**
     * 근무 종료
     */
    public void endWork(EndWorkCommand command) {
        log.info("근무 종료 - driverId: {}", command.getDriverId());

        HubDriver driver = findDriver(command.getDriverId());

        driver.endWork();

        hubDriverRepository.save(driver);

        log.info("근무 종료 완료 - driverId: {}", driver.getIdValue());
    }

    /**
     * 휴직 처리
     */
    public void deactivate(DeactivateCommand command) {
        log.info("휴직 처리 - driverId: {}", command.getDriverId());

        HubDriver driver = findDriver(command.getDriverId());

        driver.deactivate();

        hubDriverRepository.save(driver);

        log.info("휴직 처리 완료 - driverId: {}", driver.getIdValue());
    }

    /**
     * 복직 처리
     */
    public void activate(ActivateCommand command) {
        log.info("복직 처리 - driverId: {}", command.getDriverId());

        HubDriver driver = findDriver(command.getDriverId());

        driver.activate();

        hubDriverRepository.save(driver);

        log.info("복직 처리 완료 - driverId: {}", driver.getIdValue());
    }

    // ===== Private Methods =====

    private HubDriver findDriver(String driverId) {
        return hubDriverRepository.findById(HubDriverId.of(driverId))
                .orElseThrow(() -> new HubDriverException(
                        HubDriverErrorCode.HUB_DRIVER_NOT_FOUND,
                        "허브 배송 담당자를 찾을 수 없습니다: " + driverId
                ));
    }
}
