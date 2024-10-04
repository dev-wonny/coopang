package com.coopang.delivery.domain.service;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.application.request.DeliveryDto;
import com.coopang.delivery.application.service.DeliveryHubHistoryService;
import com.coopang.delivery.application.service.DeliveryUserHistoryService;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.infrastructure.repository.DeliveryJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j(topic = "DeliveryDomainService")
@Service
@Transactional
public class DeliveryDomainService {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryHubHistoryService deliveryHubHistoryService;
    private final DeliveryUserHistoryService deliveryUserHistoryService;

    public DeliveryDomainService(
            DeliveryJpaRepository deliveryJpaRepository,
            DeliveryHubHistoryService deliveryHubHistoryService,
            DeliveryUserHistoryService deliveryUserHistoryService
    ) {
        this.deliveryJpaRepository = deliveryJpaRepository;
        this.deliveryHubHistoryService = deliveryHubHistoryService;
        this.deliveryUserHistoryService = deliveryUserHistoryService;
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
                deliveryDto.getAddress2(),
                deliveryDto.getHubShipperId()
        );

        return deliveryJpaRepository.save(deliveryEntity);
    }

    // 배송 등록 - 주문 등록 후 바로 이어지는..
    // 바로 여기서 가능할거 같음
    // 허브 배송 기사, 출발 허브, 도착지 허브 찾는 feign client 여기서 넣기

    // 배송 수정 (배송지) - 주문쪽에서 배송지 수정 후 바로 이어지는..
    // 결국엔 배송지 수정이 일어나면 인접 허브 값도 같이 받자

    // 배송 상태 변경 - 목적지 허브 도착 : 허브 배송 기사님 용
    public void arrivedHub(List<DeliveryEntity> deliveries) {
        for (DeliveryEntity deliveryEntity : deliveries) {
            deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.ARRIVED_AT_DESTINATION_HUB);

            deliveryHubHistoryService.createHubHistory(
                    deliveryEntity.getDeliveryId(),
                    deliveryEntity.getDepartureHubId(),
                    deliveryEntity.getDestinationHubId(),
                    deliveryEntity.getHubShipperId(),
                    DeliveryStatusEnum.ARRIVED_AT_DESTINATION_HUB
            );
        }
        deliveryJpaRepository.saveAll(deliveries);
    }
    // 배송 상태 변경 - 목적지 도착 : 고객 배송 기사님 용
    public void arrivedDelivery(DeliveryEntity deliveryEntity) {
        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY_COMPLETED_TO_CUSTOMER);
        deliveryJpaRepository.save(deliveryEntity);

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

    // 스케줄링 - 허브 배송 준비 : 16시
    public void readyDelivery(List<DeliveryEntity> deliveries){
        updateStatusDeliveryHub(deliveries, DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_IN_PROGRESS );
    }
    // 스케줄링 - 허브 배송 물건 상차 및 주문 상태값 변경, slack 메세지 발송 : 20시
    public void sendToSlackHubDelivery(List<DeliveryEntity> deliveries){
        updateStatusDeliveryHub(deliveries, DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_COMPLETED );
    }
    // 스케줄링 - 허브 배송 출발 : 21시
    public void hubDeliveryStart(List<DeliveryEntity> deliveries){
        updateStatusDeliveryHub(deliveries, DeliveryStatusEnum.MOVING_TO_HUB );
    }
    // 스케줄링 - 고객 배송 물건 상차 및 slack 메세지 발송 : 06시
    public void sendToSlackCustomerDelivery(List<DeliveryEntity> deliveries){
        updateStatusDeliveryUser(deliveries, DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED );
    }
    // 스케줄링 - 고객 배송 출발
    public void userDeliveryStart(List<DeliveryEntity> deliveries){
        updateStatusDeliveryUser(deliveries, DeliveryStatusEnum.MOVING_TO_CUSTOMER );
    }


    // 스케줄링 공통 메서드
    private void updateStatusDeliveryHub(List<DeliveryEntity> deliveries, DeliveryStatusEnum deliveryStatus) {
        for (DeliveryEntity deliveryEntity : deliveries) {
            deliveryEntity.setDeliveryStatus(deliveryStatus);
            deliveryHubHistoryService.createHubHistory(
                    deliveryEntity.getDeliveryId(),
                    deliveryEntity.getDepartureHubId(),
                    deliveryEntity.getDestinationHubId(),
                    deliveryEntity.getHubShipperId(),
                    deliveryStatus
            );
        }
        deliveryJpaRepository.saveAll(deliveries);
    }

    private void updateStatusDeliveryUser(List<DeliveryEntity> deliveries, DeliveryStatusEnum deliveryStatus) {
        for (DeliveryEntity deliveryEntity : deliveries) {
            deliveryEntity.setDeliveryStatus(deliveryStatus);
            deliveryUserHistoryService.createUserHistory(
                    deliveryEntity.getDeliveryId(),
                    deliveryEntity.getDepartureHubId(),
                    deliveryEntity.getAddressEntity().getZipCode(),
                    deliveryEntity.getAddressEntity().getAddress1(),
                    deliveryEntity.getAddressEntity().getAddress2(),
                    deliveryEntity.getUserShipperId(),
                    deliveryStatus
            );
        }
        deliveryJpaRepository.saveAll(deliveries);
    }
}
