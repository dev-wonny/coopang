package com.coopang.delivery.presentation.controller.deliveryhistory;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.delivery.application.response.deliveryhistory.DeliveryHistoryResponseDto;
import com.coopang.delivery.application.service.DeliveryHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "DeliveryController API", description = "DeliveryController API")
@Slf4j(topic = "DeliveryController")
@RestController
@RequestMapping("/delivery-route-histories/v1")
public class DeliveryHistoryController {

    private final DeliveryHistoryService deliveryHistoryService;

    public DeliveryHistoryController(DeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    @GetMapping("/delivery/{deliveryId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    public ResponseEntity<List<DeliveryHistoryResponseDto>> getDeliveryHistory(
            @PathVariable("deliveryId") UUID deliveryId) {
        List<DeliveryHistoryResponseDto> deliveryHistoryResponseDto = deliveryHistoryService.findByDeliveryId(deliveryId);
        return new ResponseEntity<>(deliveryHistoryResponseDto, HttpStatus.OK);
    }
}
