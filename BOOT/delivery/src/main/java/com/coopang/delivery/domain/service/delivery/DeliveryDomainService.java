package com.coopang.delivery.domain.service.delivery;

import com.coopang.apicommunication.feignclient.shipper.ShipperClient;
import com.coopang.apicommunication.kafka.message.CancelDelivery;
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

    /*
    Todo : 배송지 수정시 인접허브 찾아서 해당 배송 ID 수정이 일어나게 끔하기
     */
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryHubHistoryService deliveryHubHistoryService;
    private final DeliveryUserHistoryService deliveryUserHistoryService;
    private final ShipperClient shipperClient;

    public DeliveryDomainService(
            DeliveryJpaRepository deliveryJpaRepository,
            DeliveryHubHistoryService deliveryHubHistoryService,
            DeliveryUserHistoryService deliveryUserHistoryService,
            ShipperClient shipperClient
            ) {
        this.deliveryJpaRepository = deliveryJpaRepository;
        this.deliveryHubHistoryService = deliveryHubHistoryService;
        this.deliveryUserHistoryService = deliveryUserHistoryService;
        this.shipperClient = shipperClient;
    }

    public DeliveryEntity createDelivery(
            DeliveryDto deliveryDto
    ){
        DeliveryEntity deliveryEntity = DeliveryEntity.create(
                deliveryDto.getOrderId(),
                deliveryDto.getDepartureHubId(),
                deliveryDto.getDestinationHubId(),
                deliveryDto.getZipCode(),
                deliveryDto.getAddress1(),
                deliveryDto.getAddress2()
        );

        return deliveryJpaRepository.save(deliveryEntity);
    }

    // 배송 등록 - 주문 등록 후 바로 이어지는..
    public void createProcessDelivery(ProcessDelivery processDelivery) {
        // 배송 정보 생성하기
        DeliveryEntity deliveryEntity = DeliveryEntity.create(
                processDelivery.getOrderId(),
                processDelivery.getProductHubId(),
                processDelivery.getNearHubId(),
                processDelivery.getZipCode(),
                processDelivery.getAddress1(),
                processDelivery.getAddress2()
        );
        deliveryJpaRepository.save(deliveryEntity);
    }
    // 배송 취소
    public void cancelDelivery(CancelDelivery cancelDelivery){
        DeliveryEntity deliveryEntity = deliveryJpaRepository.findByOrderId(cancelDelivery.getOrderId())
                .orElseThrow();
        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY_CANCELED);
    }

    // 배송 상태 변경 - 목적지 허브 도착 : 허브 배송 기사님 용
    public void arrivedHub(List<DeliveryEntity> deliveries,UUID hubId) {
        // 1. 고객 배송기사님 정보 가져오기
        ShipperSearchConditionRequest shipperSearchConditionRequest = new ShipperSearchConditionRequest();
        shipperSearchConditionRequest.setHubId(hubId);
        shipperSearchConditionRequest.setShipperType("SHIPPER_CUSTOMER");
        List<ShipperResponse> shippers = shipperClient.getShipperList(shipperSearchConditionRequest);

        int index = 0;


        for (DeliveryEntity delivery : deliveries) {
            delivery.setDeliveryStatus(DeliveryStatusEnum.ARRIVED_AT_DESTINATION_HUB);

            // 배달기사님 순차적으로 매핑
            delivery.setHubShipperId(shippers.get(index).getShipperId());

            // 인덱스 업데이트 (2명 순환)
            index = (index + 1) % shippers.size();
            // 허브 배송 기록 채우기
            deliveryHubHistoryService.createHubHistory(
                    delivery.getDeliveryId(),
                    delivery.getDepartureHubId(),
                    delivery.getDestinationHubId(),
                    delivery.getHubShipperId(),
                    delivery.getDeliveryStatus()
            );

            // 고객 배송 기록 채우기
            delivery.setDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_IN_PROGRESS);
            deliveryUserHistoryService.createUserHistory(
                    delivery.getDeliveryId(),
                    delivery.getDepartureHubId(),
                    delivery.getAddressEntity().getZipCode(),
                    delivery.getAddressEntity().getAddress1(),
                    delivery.getAddressEntity().getAddress2(),
                    delivery.getUserShipperId(),
                    delivery.getDeliveryStatus()
            );
        }
    }
    // 배송 상태 변경 - 목적지 도착 : 고객 배송 기사님 용
    public void arrivedDelivery(DeliveryEntity deliveryEntity) {
        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY_COMPLETED_TO_CUSTOMER);

        deliveryUserHistoryService.createUserHistory(
                deliveryEntity.getDeliveryId(),
                deliveryEntity.getDepartureHubId(),
                deliveryEntity.getAddressEntity().getZipCode(),
                deliveryEntity.getAddressEntity().getAddress1(),
                deliveryEntity.getAddressEntity().getAddress2(),
                deliveryEntity.getUserShipperId(),
                DeliveryStatusEnum.DELIVERY_COMPLETED_TO_CUSTOMER
        );
    }
}
