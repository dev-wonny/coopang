package com.coopang.delivery.presentation.controller.delivery;

import com.coopang.apicommunication.feignclient.order.OrderClientService;
import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.order.response.OrderResponse;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.delivery.application.request.delivery.DeliveryDto;
import com.coopang.delivery.application.response.delivery.DeliveryResponseDto;
import com.coopang.delivery.application.service.delivery.DeliveryService;
import com.coopang.delivery.application.service.message.delivery.DeliveryMessageService;
import com.coopang.delivery.application.service.schedule.DeliveryScheduleService;
import com.coopang.delivery.domain.service.delivery.DeliveryDomainService;
import com.coopang.delivery.presentation.request.delivery.DeliveryRequestDto;
import com.coopang.delivery.presentation.request.delivery.DeliverySearchCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "DeliveryController API", description = "DeliveryController API")
@Slf4j(topic = "DeliveryController")
@RestController
@RequestMapping("/deliveries/v1")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final ModelMapperConfig mapperConfig;
    private final DeliveryMessageService deliveryMessageService;
    private final DeliveryScheduleService deliveryScheduleService;
    private final FeignConfig feignConfig;
    private final OrderClientService orderClientService;


    public DeliveryController(
            DeliveryService deliveryService,
            ModelMapperConfig mapperConfig,
            DeliveryMessageService deliveryMessageService,
            DeliveryScheduleService deliveryScheduleService,
            FeignConfig feignConfig,
            OrderClientService orderClientService
    ) {
        this.deliveryService = deliveryService;
        this.mapperConfig = mapperConfig;
        this.deliveryMessageService = deliveryMessageService;
        this.deliveryScheduleService = deliveryScheduleService;
        this.feignConfig = feignConfig;
        this.orderClientService = orderClientService;
    }

    @PostMapping("/delivery")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<DeliveryResponseDto> createDelivery(@Valid @RequestBody DeliveryRequestDto deliveryRequestDto) {
        DeliveryDto deliveryDto = mapperConfig.strictMapper().map(deliveryRequestDto, DeliveryDto.class);
        DeliveryResponseDto deliveryResponseDto = deliveryService.createDelivery(deliveryDto);
        return new ResponseEntity<>(deliveryResponseDto, HttpStatus.CREATED);
    }

    // 배송 단건 조회
    @GetMapping("/delivery/{deliveryId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    public ResponseEntity<DeliveryResponseDto> getDeliveryById(
            @PathVariable UUID deliveryId,
            @RequestParam(required = false) UUID departureHubId
    ) {
        final DeliveryResponseDto deliveryInfo;

        if (departureHubId != null) {
            deliveryInfo = deliveryService.getDeliveryByIdWithHubId(deliveryId, departureHubId);
        } else {
            deliveryInfo = deliveryService.getDeliveryById(deliveryId);
        }

        return new ResponseEntity<>(deliveryInfo, HttpStatus.OK);
    }

    // 배송 전체 조회
    @GetMapping("/delivery")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    public ResponseEntity<Page<DeliveryResponseDto>> getAllDeliveries(
            Pageable pageable,
            @RequestParam(required = false) UUID departureHubId
    ) {
        final Page<DeliveryResponseDto> deliveries;

        if (departureHubId != null) {
            deliveries = deliveryService.getAllDeliveriesWithHubId(pageable, departureHubId);
        } else {
            deliveries = deliveryService.getAllDeliveries(pageable);
        }

        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    // 특정 배송 조회 (주문ID)
    @GetMapping("/order/{orderId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<DeliveryResponseDto> getDeliveryByOrderId(
            @PathVariable UUID orderId,
            @RequestParam(required = false) UUID hubId
    ) {
        final DeliveryResponseDto deliveryInfo;

        if (hubId != null) {
            deliveryInfo = deliveryService.getDeliveryByOrderIdWithHubId(orderId, hubId);
        } else {
            deliveryInfo = deliveryService.getDeliveryByOrderId(orderId);
        }

        return new ResponseEntity<>(deliveryInfo, HttpStatus.OK);
    }

    // 배송 검색 (도착 허브에 대해서)
    @GetMapping("/delivery/search")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Page<DeliveryResponseDto>> searchDeliveries(DeliverySearchCondition condition, Pageable pageable) {
        Page<DeliveryResponseDto> deliveries = deliveryService.searchDeliveries(condition, pageable);
        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @PatchMapping("/delivery/{deliveryId}/changeShipper")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<DeliveryResponseDto> changeDeliveryShipper(@PathVariable UUID deliveryId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/delivery/{deliveryId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<DeliveryResponseDto> updateDeliveryRoute(@PathVariable UUID deliveryId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 배송 상태 변경 - 목적지 허브 도착
    @PutMapping("/hub/{destinationHubId}/hubShipper/{hubShipperId}/arrived")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    public ResponseEntity<Void> arrivedHub(
            @PathVariable UUID destinationHubId,
            @PathVariable UUID hubShipperId
    ) {
        deliveryService.arrivedHub(destinationHubId, hubShipperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 16시 종합본
    @GetMapping("deliveries/v1/delivery/16")
    @Secured(UserRoleEnum.Authority.MASTER)
    public ResponseEntity<Void> mapHubDelivery() {
        deliveryScheduleService.readyDelivery();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 배송 상태 변경 - 목적지 도착
    @PutMapping("delivery/{deliveryId}/arrived")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    public ResponseEntity<Void> arrivedCustomer(
            @PathVariable UUID deliveryId
    ) {
        deliveryService.arrivedDelivery(deliveryId);
        deliveryMessageService.processUserChangeStatus(deliveryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 특정 배송 삭제
    @DeleteMapping("/delivery/{deliveryId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Void> deleteDelivery(@PathVariable UUID deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/delivery/test")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<List<OrderResponse>> getList() {
        feignConfig.changeHeaderRoleToServer();
        List<OrderResponse> orders = orderClientService.getOrderList();
        feignConfig.resetRole();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/delivery/test/20")
    @Secured({UserRoleEnum.Authority.MASTER})
    public ResponseEntity<String> sendToSlackHubDelivery() {
        deliveryScheduleService.sendToSlackHubDelivery();
        return new ResponseEntity<>("// 스케줄링 - 허브 배송 물건 상차 및 주문 상태값 변경 : 20시", HttpStatus.OK);
    }

    @PostMapping("/delivery/test/21")
    @Secured({UserRoleEnum.Authority.MASTER})
    public ResponseEntity<String> hubDeliveryStart() {
        deliveryScheduleService.hubDeliveryStart();
        return new ResponseEntity<>("스케줄링 - 허브 배송 출발 : 21시", HttpStatus.OK);
    }

    @PostMapping("/delivery/test/6")
    @Secured({UserRoleEnum.Authority.MASTER})
    public ResponseEntity<String> sendToSlackCustomerDelivery() {
        deliveryScheduleService.sendToSlackCustomerDelivery();
        return new ResponseEntity<>("고객 배송 물건 상차 및 slack 메세지 발송 : 06시", HttpStatus.OK);
    }

    @PostMapping("/delivery/test/8")
    @Secured({UserRoleEnum.Authority.MASTER})
    public ResponseEntity<String> userDeliveryStart() {
        deliveryScheduleService.userDeliveryStart();
        return new ResponseEntity<>("고객 배송 출발 : 08시", HttpStatus.OK);
    }
}
