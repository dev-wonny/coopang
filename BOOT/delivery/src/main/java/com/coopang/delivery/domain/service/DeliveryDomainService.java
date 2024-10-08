package com.coopang.delivery.domain.service;

import com.coopang.apicommunication.kafka.message.CancelDelivery;
import com.coopang.apicommunication.kafka.message.CompleteDelivery;
import com.coopang.apicommunication.kafka.message.ProcessDelivery;
import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.application.request.DeliveryDto;
import com.coopang.delivery.application.service.DeliveryHubHistoryService;
import com.coopang.delivery.application.service.DeliveryUserHistoryService;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.infrastructure.repository.DeliveryJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public DeliveryDomainService(
            DeliveryJpaRepository deliveryJpaRepository,
            DeliveryHubHistoryService deliveryHubHistoryService,
            DeliveryUserHistoryService deliveryUserHistoryService,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
            ) {
        this.deliveryJpaRepository = deliveryJpaRepository;
        this.deliveryHubHistoryService = deliveryHubHistoryService;
        this.deliveryUserHistoryService = deliveryUserHistoryService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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
    public void createProcessDelivery(ProcessDelivery processDelivery) {
        // feign client : 허브 배송 기사, 출발 허브 값 가져오기 (hub)
        final UUID hubShipperId = UUID.randomUUID();
        final UUID departureHubId = UUID.randomUUID();
        // feign client : 도착지 허브 값 가져오기 (user)
        final UUID destinationHubId = UUID.randomUUID();
        // 배송 정보 생성하기
        DeliveryEntity deliveryEntity = DeliveryEntity.create(
                processDelivery.getOrderId(),
                departureHubId,
                destinationHubId,
                processDelivery.getZipCode(),
                processDelivery.getAddress1(),
                processDelivery.getAddress2(),
                hubShipperId
        );

        try {
            CompleteDelivery completeDelivery = new CompleteDelivery();
            completeDelivery.setOrderId(processDelivery.getOrderId());
            completeDelivery.setHubId(departureHubId);
            completeDelivery.setNearHubId(destinationHubId);

            final String sendMessage = objectMapper.writeValueAsString(completeDelivery);
            kafkaTemplate.send("complete-delivery", sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        deliveryJpaRepository.save(deliveryEntity);
    }
    // 배송 취소
    public void cancelDelivery(CancelDelivery cancelDelivery){
        DeliveryEntity deliveryEntity = deliveryJpaRepository.findByOrderId(cancelDelivery.getOrderId())
                .orElseThrow();
        deliveryEntity.setDeliveryStatus(DeliveryStatusEnum.DELIVERY_CANCELED);
    }
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
