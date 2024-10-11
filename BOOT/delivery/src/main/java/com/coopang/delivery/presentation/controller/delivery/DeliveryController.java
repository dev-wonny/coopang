package com.coopang.delivery.presentation.controller.delivery;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.delivery.application.request.delivery.DeliveryDto;
import com.coopang.delivery.application.response.delivery.DeliveryResponseDto;
import com.coopang.delivery.application.service.delivery.DeliveryService;
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

import java.util.UUID;

@Tag(name = "DeliveryController API", description = "DeliveryController API")
@Slf4j(topic = "DeliveryController")
@RestController
@RequestMapping("/deliveries/v1")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final ModelMapperConfig mapperConfig;

    public DeliveryController(
            DeliveryService deliveryService,
            ModelMapperConfig mapperConfig
    ) {
        this.deliveryService = deliveryService;
        this.mapperConfig = mapperConfig;
    }

    @Secured({"ROLE_MASTER","ROLE_HUB_MANAGER"})
    @PostMapping("/delivery")
    public ResponseEntity<DeliveryResponseDto> createDelivery(@Valid @RequestBody DeliveryRequestDto deliveryRequestDto){
        DeliveryDto deliveryDto = mapperConfig.strictMapper().map(deliveryRequestDto, DeliveryDto.class);
        DeliveryResponseDto deliveryResponseDto = deliveryService.createDelivery(deliveryDto);
        return new ResponseEntity<>(deliveryResponseDto,HttpStatus.CREATED);
    }

    // 배송 단건 조회
//    @Secured("ROLE_MASTER")
    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> getDeliveryById(
            @PathVariable UUID deliveryId,
            @RequestParam(required = false) UUID departureHubId
    ){
        final DeliveryResponseDto deliveryInfo;

        if (departureHubId != null) {
            deliveryInfo = deliveryService.getDeliveryByIdWithHubId(deliveryId,departureHubId);
        } else {
            deliveryInfo = deliveryService.getDeliveryById(deliveryId);
        }

        return new ResponseEntity<>(deliveryInfo,HttpStatus.OK);
    }

    // 배송 전체 조회
    @Secured("ROLE_MASTER")
    @GetMapping("/delivery")
    public ResponseEntity<Page<DeliveryResponseDto>> getAllDeliveries(
            Pageable pageable,
            @RequestParam(required = false) UUID departureHubId
    ){
        final Page<DeliveryResponseDto> deliveries;

        if (departureHubId != null) {
            deliveries = deliveryService.getAllDeliveriesWithHubId(pageable,departureHubId);
        } else {
            deliveries = deliveryService.getAllDeliveries(pageable);
        }

        return new ResponseEntity<>(deliveries,HttpStatus.OK);
    }

    // 특정 배송 조회 (주문ID)
    @Secured("ROLE_MASTER")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryResponseDto> getDeliveryByOrderId(
            @PathVariable UUID orderId,
            @RequestParam(required = false) UUID hubId
    ){
        final DeliveryResponseDto deliveryInfo;

        if (hubId != null) {
            deliveryInfo = deliveryService.getDeliveryByOrderIdWithHubId(orderId,hubId);
        } else {
            deliveryInfo = deliveryService.getDeliveryByOrderId(orderId);
        }

        return new ResponseEntity<>(deliveryInfo,HttpStatus.OK);
    }

    // 배송 검색 (도착 허브에 대해서)
    @Secured("ROLE_MASTER")
    @GetMapping("/delivery/search")
    public ResponseEntity<Page<DeliveryResponseDto>> searchDeliveries(DeliverySearchCondition condition,Pageable pageable){
        Page<DeliveryResponseDto> deliveries = deliveryService.searchDeliveries(condition,pageable);
        return new ResponseEntity<>(deliveries,HttpStatus.OK);
    }

    @Secured("ROLE_MASTER")
    @PatchMapping("/delivery/{deliveryId}/changeShipper")
    public ResponseEntity<DeliveryResponseDto> changeDeliveryShipper(@PathVariable UUID deliveryId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_MASTER")
    @PutMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> updateDeliveryRoute(@PathVariable UUID deliveryId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 배송 상태 변경 - 목적지 허브 도착
    @Secured("ROLE_MASTER")
    @PutMapping("/hub/{destinationHubId}/hubShipper/{hubShipperId}/arrived")
    public ResponseEntity<Void> arrivedHub(
            @PathVariable UUID destinationHubId,
            @PathVariable UUID hubShipperId
            ){
        deliveryService.arrivedHub(destinationHubId, hubShipperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 배송 상태 변경 - 목적지 도착
    @Secured("ROLE_MASTER")
    @PutMapping("delivery/{deliveryId}/arrived")
    public ResponseEntity<Void> arrivedCustomer(
            @PathVariable UUID deliveryId
    ){
        deliveryService.arrivedDelivery(deliveryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 특정 배송 삭제
    @Secured("ROLE_MASTER")
    @DeleteMapping("/delivery/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable UUID deliveryId){
        deliveryService.deleteDelivery(deliveryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
