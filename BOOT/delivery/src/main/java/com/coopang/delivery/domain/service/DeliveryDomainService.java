package com.coopang.delivery.domain.service;

import com.coopang.delivery.application.request.DeliveryDto;
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

    public DeliveryDomainService(
            DeliveryJpaRepository deliveryJpaRepository
    ) {
        this.deliveryJpaRepository = deliveryJpaRepository;
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
//            deliveries.setDeliveryStatus(DeliveryStatusEnum.ARRIVED AT DESTINTION HUB);
        }
        deliveryJpaRepository.saveAll(deliveries);
    }
    // 배송 상태 변경 - 목적지 도착 : 고객 배송 기사님 용
    public void arrivedDelivery(DeliveryEntity deliveryEntity) {
//        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY COMPLETED TO CUSTOMER);
        deliveryJpaRepository.save(deliveryEntity);
    }

//    // 스케줄링 - 허브 배송 출발
//    public void hubDeliveryStart(List<DeliveryEntity> deliveries){
//        updateStatus(deliveries, DeliveryStatusEnum.MOVING_TO_HUB );
//    }
//
//    // 스케줄링 - 고객 배송 출발
//    public void userDeliveryStart(List<DeliveryEntity> deliveries){
//        updateStatus(deliveries, DeliveryStatusEnum.MOVING_TO_CUSTOMER );
//    }
//
//    // 스케줄링 공통 메서드
//    private void updateStatus(List<DeliveryEntity> deliveries, DeliveryStatusEnum deliveryStatus) {
//        for (DeliveryEntity deliveryEntity : deliveries) {
////            deliveryEntity.setDeliveryStatus(deliveryStatus);
//        }
//        deliveryJpaRepository.saveAll(deliveries);
//    }
}
