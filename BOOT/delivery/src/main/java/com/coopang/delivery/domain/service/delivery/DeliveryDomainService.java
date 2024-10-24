package com.coopang.delivery.domain.service.delivery;

import com.coopang.apicommunication.feignclient.shipper.ShipperClientService;
import com.coopang.apicommunication.kafka.message.CancelDelivery;
import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.apidata.application.shipper.request.ShipperSearchConditionRequest;
import com.coopang.apidata.application.shipper.response.ShipperResponse;
import com.coopang.delivery.application.request.delivery.DeliveryDto;
import com.coopang.delivery.application.service.deliveryhubhistory.DeliveryHubHistoryService;
import com.coopang.delivery.application.service.deliveryuserhistory.DeliveryUserHistoryService;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.infrastructure.repository.delivery.DeliveryJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "DeliveryDomainService")
@Service
@Transactional
public class DeliveryDomainService {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryHubHistoryService deliveryHubHistoryService;
    private final DeliveryUserHistoryService deliveryUserHistoryService;
    private final ShipperClientService shipperClientService;
    private final FeignConfig feignConfig;

    public DeliveryDomainService(
            DeliveryJpaRepository deliveryJpaRepository
            , DeliveryHubHistoryService deliveryHubHistoryService
            , DeliveryUserHistoryService deliveryUserHistoryService
            , ShipperClientService shipperClientService
            , FeignConfig feignConfig
    ) {
        this.deliveryJpaRepository = deliveryJpaRepository;
        this.deliveryHubHistoryService = deliveryHubHistoryService;
        this.deliveryUserHistoryService = deliveryUserHistoryService;
        this.shipperClientService = shipperClientService;
        this.feignConfig = feignConfig;
    }

    public DeliveryEntity createDelivery(
            DeliveryDto deliveryDto
    ) {
        DeliveryEntity deliveryEntity = DeliveryEntity.create(
                deliveryDto.getOrderId()
                , deliveryDto.getDepartureHubId()
                , deliveryDto.getDestinationHubId()
                , deliveryDto.getZipCode()
                , deliveryDto.getAddress1()
                , deliveryDto.getAddress2()
                , deliveryDto.getHubShipperId()
        );

        return deliveryJpaRepository.save(deliveryEntity);
    }

    // 배송 등록 - 주문 등록 후 바로 이어지는..
    public DeliveryEntity createProcessDelivery(
            UUID orderId
            , UUID productHubId
            , UUID nearHubId
            , String zipCode
            , String address1
            , String address2
            , UUID hubShipperId
    ) {
        // 배송 정보 생성하기
        DeliveryEntity deliveryEntity = DeliveryEntity.create(
                orderId
                , productHubId
                , nearHubId
                , zipCode
                , address1
                , address2
                , hubShipperId
        );
        return deliveryJpaRepository.save(deliveryEntity);
    }

    // 배송 취소
    public void cancelDelivery(CancelDelivery cancelDelivery) {
        DeliveryEntity deliveryEntity = deliveryJpaRepository.findByOrderId(cancelDelivery.getOrderId())
                .orElseThrow();
        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY_CANCELED);
    }

    // 배송 상태 변경 - 목적지 허브 도착 : 허브 배송 기사님 용
    public void arrivedHub(List<DeliveryEntity> deliveries, UUID hubId) {
        for (DeliveryEntity hubDelivery : deliveries) {
            hubDelivery.setDeliveryStatus(DeliveryStatusEnum.ARRIVED_AT_DESTINATION_HUB);

            // 허브 배송 기록 채우기
            deliveryHubHistoryService.createHubHistory(
                    hubDelivery.getDeliveryId()
                    , hubDelivery.getDepartureHubId()
                    , hubDelivery.getDestinationHubId()
                    , hubDelivery.getHubShipperId()
                    , hubDelivery.getDeliveryStatus()
            );
        }
        // 1. 고객 배송기사님 정보 가져오기
        ShipperSearchConditionRequest shipperSearchConditionRequest = new ShipperSearchConditionRequest();
        shipperSearchConditionRequest.setHubId(hubId);
        shipperSearchConditionRequest.setShipperType("SHIPPER_CUSTOMER");
        feignConfig.changeHeaderRoleToServer();
        List<ShipperResponse> shippers = shipperClientService.getShipperList(shipperSearchConditionRequest);
        feignConfig.resetRole();

        int index = 0;


        for (DeliveryEntity userDelivery : deliveries) {

            // 배달기사님 순차적으로 매핑
            userDelivery.setHubShipperId(shippers.get(index).getShipperId());

            // 인덱스 업데이트 (2명 순환)
            index = (index + 1) % shippers.size();

            // 고객 배송 기록 채우기
            userDelivery.setDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_IN_PROGRESS);
            deliveryUserHistoryService.createUserHistory(
                    userDelivery.getDeliveryId()
                    , userDelivery.getDepartureHubId()
                    , userDelivery.getAddressEntity().getZipCode()
                    , userDelivery.getAddressEntity().getAddress1()
                    , userDelivery.getAddressEntity().getAddress2()
                    , userDelivery.getUserShipperId()
                    , userDelivery.getDeliveryStatus()
            );
        }
    }

    // 배송 상태 변경 - 목적지 도착 : 고객 배송 기사님 용
    public void arrivedDelivery(DeliveryEntity deliveryEntity) {
        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY_COMPLETED_TO_CUSTOMER);

        deliveryUserHistoryService.createUserHistory(
                deliveryEntity.getDeliveryId()
                , deliveryEntity.getDepartureHubId()
                , deliveryEntity.getAddressEntity().getZipCode()
                , deliveryEntity.getAddressEntity().getAddress1()
                , deliveryEntity.getAddressEntity().getAddress2()
                , deliveryEntity.getUserShipperId()
                , DeliveryStatusEnum.DELIVERY_COMPLETED_TO_CUSTOMER
        );
    }
}
