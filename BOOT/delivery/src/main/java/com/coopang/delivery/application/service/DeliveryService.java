package com.coopang.delivery.application.service;

import com.coopang.delivery.application.request.DeliveryDto;
import com.coopang.delivery.application.response.DeliveryResponseDto;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.domain.repository.DeliveryRepository;
import com.coopang.delivery.domain.service.DeliveryDomainService;
import com.coopang.delivery.presentation.request.DeliverySearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            DeliveryDomainService deliveryDomainService
    ) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryDomainService = deliveryDomainService;
    }
    // 등록 //

    // 배송 등록 (예외적인 상황 : 배송을 다시 등록해야하는 상황,...)
    public DeliveryResponseDto createDelivery(DeliveryDto deliveryDto){
        DeliveryEntity deliveryEntity = deliveryDomainService.createDelivery(deliveryDto);
        log.debug("createDelivery");
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
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
    public DeliveryResponseDto getDeliveryByIdWithHubId(UUID deliveryId, UUID hubId){
        DeliveryEntity deliveryEntity = deliveryRepository.findByIdHubId(deliveryId,hubId)
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
    public DeliveryResponseDto getDeliveryByOrderIdWithHubId(UUID orderId,UUID hubId) {
        DeliveryEntity deliveryEntity = deliveryRepository.findByOrderIdHubId(orderId,hubId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found. orderId=" + orderId));
        return DeliveryResponseDto.fromDelivery(deliveryEntity);
    }

    // 배송 전체 조회 (페이징 및 정렬 지원)
    public Page<DeliveryResponseDto> getAllDeliveries(Pageable pageable){
        Page<DeliveryEntity> deliveries = deliveryRepository.findAll(pageable);
        return deliveries.map(DeliveryResponseDto::fromDelivery);
    }

    // 배송 전체 조회 (페이징 및 정렬 지원)
    public Page<DeliveryResponseDto> getAllDeliveriesWithHubId(Pageable pageable,UUID hubId){
        Page<DeliveryEntity> deliveries = deliveryRepository.findAllHubId(pageable,hubId);
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
    // 스케줄링 - 허브 배송 물건 상차 및 주문 상태값 변경, slack 메세지 발송 : 20시
    // 스케줄링 - 허브 배송 출발 : 21시
//    @Scheduled(cron = "0 0 21 * * *")
//    public void hubDeliveryStart(){
//        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_COMPLETED);
//        deliveryDomainService.hubDeliveryStart(deliveries);
//    }
//    // 스케줄링 - 고객 배송 물건 상차 및 slack 메세지 발송 : 06시
//    // 스케줄링 - 고객 배송 출발 : 08시
//    @Scheduled(cron = "0 0 8 * * *")
//    public void userDeliveryStart(){
//        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED);
//        deliveryDomainService.hubDeliveryStart(deliveries);
//    }

    // 삭제 //

    // 특정 배송 삭제
    public void deleteDelivery(UUID deliveryId){
        DeliveryEntity deliveryEntity = findByDeliveryId(deliveryId);
        deliveryEntity.setDeleted(true);
        log.debug("deletedelivery deliveryId : {}", deliveryId);
    }

    // 공통 메서드 //

    // 배달ID로 값 찾기
    private DeliveryEntity findByDeliveryId(UUID deliveryId){
        return deliveryRepository.findById(deliveryId)
//                .filter(o-> !o.getIsDeleted) // 삭제가 되지 않은 값만 불러오기 위함인데 왜 getter가 안되죵??
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found. deliveryId=" + deliveryId));
    }

//    // 배송 상태값으로 찾기
//    private DeliveryEntity findByDeliveryStatus(DeliveryStatusEnum deliveryStatus){
//        return deliveryRepository.findAllByDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED);
//
//    }

}
