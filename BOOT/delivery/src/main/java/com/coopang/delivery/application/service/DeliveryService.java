package com.coopang.delivery.application.service;

import com.coopang.apicommunication.kafka.message.CancelDelivery;
import com.coopang.apicommunication.kafka.message.ProcessDelivery;
import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.application.request.delivery.DeliveryDto;
import com.coopang.delivery.application.response.delivery.DeliveryResponseDto;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.domain.repository.DeliveryRepository;
import com.coopang.delivery.domain.service.DeliveryDomainService;
import com.coopang.delivery.presentation.request.DeliverySearchCondition;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "DeliveryService")
@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryDomainService deliveryDomainService;
    private final ObjectMapper objectMapper;

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            DeliveryDomainService deliveryDomainService,
            ObjectMapper objectMapper
    ) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryDomainService = deliveryDomainService;
        this.objectMapper = objectMapper;
    }
    // 등록 //

    // 배송 등록 (예외적인 상황 : 배송을 다시 등록해야하는 상황,...)
    public DeliveryResponseDto createDelivery(DeliveryDto deliveryDto){
        DeliveryEntity deliveryEntity = deliveryDomainService.createDelivery(deliveryDto);
        log.debug("createDelivery");
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
    }

    // 배송 등록 기본적인 흐름
    @KafkaListener(topics = "process", groupId = "my-group")
    public void processDelivery(String message){
        try {
            ProcessDelivery processDelivery = objectMapper.readValue(message, ProcessDelivery.class);
            deliveryDomainService.createProcessDelivery(processDelivery);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // 조회 //

    // 특정 배송 조회
    public DeliveryResponseDto getDeliveryById(UUID deliveryId)
    {
        DeliveryEntity deliveryEntity = findByDeliveryId(deliveryId);
        log.debug("getDeliveryById deliveryId : {}", deliveryId);
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
    }
    // 특정 배송 조회 - hubId
    public DeliveryResponseDto getDeliveryByIdWithHubId(UUID deliveryId, UUID departureHubId){
        DeliveryEntity deliveryEntity = deliveryRepository.findByDeliveryIdAndDepartureHubId(deliveryId,departureHubId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found. deliveryId=" + deliveryId));
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
    }

    // 특정 배송 조회 (주문 아이디로)
    public DeliveryResponseDto getDeliveryByOrderId(UUID orderId){
        DeliveryEntity deliveryEntity = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found. orderId=" + orderId));
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
    }

    // 특정 배송 조회 (주문 아이디로) - hubId
    public DeliveryResponseDto getDeliveryByOrderIdWithHubId(UUID orderId,UUID departureHubId) {
        DeliveryEntity deliveryEntity = deliveryRepository.findByOrderIdAndDepartureHubId(orderId,departureHubId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found. orderId=" + orderId));
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
    }

    // 배송 전체 조회 (페이징 및 정렬 지원)
    public Page<DeliveryResponseDto> getAllDeliveries(Pageable pageable){
        Page<DeliveryEntity> deliveries = deliveryRepository.findAll(pageable);
        return deliveries.map(DeliveryResponseDto::fromDelivery);
    }

    // 배송 전체 조회 (페이징 및 정렬 지원)
    public Page<DeliveryResponseDto> getAllDeliveriesWithHubId(Pageable pageable,UUID departureHubId){
        Page<DeliveryEntity> deliveries = deliveryRepository.findAllByDepartureHubId(pageable,departureHubId);
        return deliveries.map(DeliveryResponseDto::fromDelivery);
    }

    // 배송 검색 (페이징, 정렬, 키워드 검색)
    public Page<DeliveryResponseDto> searchDeliveries(DeliverySearchCondition condition, Pageable pageable){
        Page<DeliveryEntity> deliveries = deliveryRepository.search(condition,pageable);
        return deliveries.map(DeliveryResponseDto::fromDelivery);
    }

    // 수정 //

    // 배송지 수정

    // 배송 상태 변경 - 목적지 허브 도착 : 허브 배송 기사님 용
    public void arrivedHub(UUID destinationHubId, UUID hubSipperId) {
        List<DeliveryEntity> deliveries = deliveryRepository.findAllByDestinationHubIdAndHubShipperId(destinationHubId,hubSipperId);
        deliveryDomainService.arrivedHub(deliveries);
        // 추후에 고객 배송 기사에게 배송건 할당
    }

    // 배송 상태 변경 - 목적지 도착 : 고객 배송 기사님 용
    public void arrivedDelivery(UUID deliveryId) {
        DeliveryEntity deliveryEntity = findByDeliveryId(deliveryId);
        deliveryDomainService.arrivedDelivery(deliveryEntity);
    }
    // 스케줄링 - 허브 배송 준비 : 16시
    @Scheduled(cron = "0 0 16 * * *")
    public void readyDelivery(){
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.PENDING);
        deliveryDomainService.readyDelivery(deliveries);
    }

    // 스케줄링 - 허브 배송 물건 상차 및 주문 상태값 변경, slack 메세지 발송 : 20시
    @Scheduled(cron = "0 0 20 * * *")
    public void sendToSlackHubDelivery(){
        // 허브쪽에서 허브정보들 가져오기
        // 허브들마다 정보 전달하기
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_IN_PROGRESS);
        deliveryDomainService.sendToSlackHubDelivery(deliveries);
        // api쪽으로 해당 정보 전달하기
    }

    // 스케줄링 - 허브 배송 출발 : 21시
    @Scheduled(cron = "0 0 21 * * *")
    public void hubDeliveryStart(){
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_COMPLETED);
        deliveryDomainService.hubDeliveryStart(deliveries);
    }

    // 스케줄링 - 고객 배송 물건 상차 및 slack 메세지 발송 : 06시
    @Scheduled(cron = "0 0 6 * * *")
    public void sendToSlackCustomerDelivery(){
        //
        // 허브들마다 정보 전달하기
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_IN_PROGRESS);
        deliveryDomainService.sendToSlackCustomerDelivery(deliveries);
        // api쪽으로 해당 정보 전달하기
    }

    // 스케줄링 - 고객 배송 출발 : 08시
    @Scheduled(cron = "0 0 8 * * *")
    public void userDeliveryStart(){
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED);
        deliveryDomainService.userDeliveryStart(deliveries);
    }

    // 삭제 //

    // 특정 배송 삭제
    public void deleteDelivery(UUID deliveryId){
        DeliveryEntity deliveryEntity = findByDeliveryId(deliveryId);
        deliveryEntity.setDeleted(true);
        log.debug("deletedelivery deliveryId : {}", deliveryId);
    }

    // 취소 //

    // 배송 취소
    @KafkaListener(topics = "cancel_delivery", groupId = "my-group")
    public void cancelDelivery(String message){
        try {
            CancelDelivery cancelDelivery = objectMapper.readValue(message, CancelDelivery.class);
            deliveryDomainService.cancelDelivery(cancelDelivery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공통 메서드 //

    // 배달ID로 값 찾기
    private DeliveryEntity findByDeliveryId(UUID deliveryId){
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found. deliveryId=" + deliveryId));
    }

    // 배송 상태값으로 찾기
    private List<DeliveryEntity> findByDeliveryStatus(DeliveryStatusEnum deliveryStatus){
        return deliveryRepository.findAllByDeliveryStatus(deliveryStatus);
    }
}
