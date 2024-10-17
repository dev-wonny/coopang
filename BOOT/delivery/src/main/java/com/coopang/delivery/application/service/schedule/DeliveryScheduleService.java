package com.coopang.delivery.application.service.schedule;

import com.coopang.apicommunication.feignclient.shipper.ShipperClientService;
import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.apidata.application.shipper.request.ShipperSearchConditionRequest;
import com.coopang.apidata.application.shipper.response.ShipperResponse;
import com.coopang.delivery.application.service.deliveryhubhistory.DeliveryHubHistoryService;
import com.coopang.delivery.application.service.deliveryuserhistory.DeliveryUserHistoryService;
import com.coopang.delivery.application.service.message.delivery.DeliveryMessageService;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.domain.repository.delivery.DeliveryRepository;
import com.coopang.delivery.domain.service.delivery.DeliveryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j(topic = "DeliveryScheduleService")
@Service
@Transactional
public class DeliveryScheduleService {

    private final DeliveryHubHistoryService deliveryHubHistoryService;
    private final DeliveryUserHistoryService deliveryUserHistoryService;
    private final DeliveryMessageService deliveryMessageService;
    private final DeliveryRepository deliveryRepository;
    private final ShipperClientService shipperClientService;
    private final FeignConfig feignConfig;
    private final DeliveryDomainService deliveryDomainService;

    public DeliveryScheduleService(
            DeliveryHubHistoryService deliveryHubHistoryService,
            DeliveryUserHistoryService deliveryUserHistoryService,
            DeliveryMessageService deliveryMessageService,
            ShipperClientService shipperClientService,
            FeignConfig feignConfig,
            DeliveryRepository deliveryRepository,
            DeliveryDomainService deliveryDomainService
    ) {
        this.deliveryHubHistoryService = deliveryHubHistoryService;
        this.deliveryUserHistoryService = deliveryUserHistoryService;
        this.deliveryMessageService = deliveryMessageService;
        this.shipperClientService = shipperClientService;
        this.feignConfig = feignConfig;
        this.deliveryRepository = deliveryRepository;
        this.deliveryDomainService = deliveryDomainService;
    }

        /*
    < 스케줄링 >
    16시 - 배송 상태값 : 허브 배송 배차 중
        - 허브 배송기록 테이블에 기록
    20시 - 배송 상태값 : 허브 배송 배차 완료
        - 주문 상태값 : SHIPPED (주문 취소 X)
        - Slack Message 발송
        - 허브 배송기록 테이블에 기록
    21시 - 배송 상태값 : 허브 이동중
        - 허브 배송기록 테이블에 기록
    허브 도착시 - 배송 상태값 : 허브 도착
     */

    // 스케줄링 - 허브 배송 준비 : 16시
//    @Scheduled(cron = "0 0 16 * * *")
//    @Scheduled(cron = "0 44 2 * * *")
    public void readyDelivery() {
        /*
        1. order_server 에서 주문등록 완료된 애들만 가져오기
        2. 배송 등록하면서 허브배송 기사님 같이 등록하기
         */
        OrderSearchConditionRequest orderSearchConditionRequest = new OrderSearchConditionRequest();
        OrderSearchConditionRequest.setOrderStatus("PENDING");
        feignConfig.changeHeaderRoleToServer();
        List<OrderResponse> orders = orderService.getOrderList(orderSearchConditionRequest);
        feignConfig.resetRole();

        // 2. 허브 배송기사님 정보 가져오기
        ShipperSearchConditionRequest shipperSearchConditionRequest = new ShipperSearchConditionRequest();
        shipperSearchConditionRequest.setShipperType("SHIPPER_HUB");
        feignConfig.changeHeaderRoleToServer();
        List<ShipperResponse> shippers = shipperClientService.getShipperList(shipperSearchConditionRequest);
        feignConfig.resetRole();

        // 3. 배송기사님 정보를 Map으로 변환 (허브 ID를 키로 사용)
        Map<UUID, UUID> shipperMap = new HashMap<>();
        for (ShipperResponse shipper : shippers) {
            shipperMap.put(shipper.getHubId(), shipper.getShipperId());
        }

        // 4. 배송 상태 업데이트 및 배송기사님 매핑
        for (OrderResponse order : orders) {
            // 출발지 허브 ID에 대한 배송기사님 찾기
            UUID hubShipperId = shipperMap.get(order.getProductHubId());

            // 1. 배송 등록
            DeliveryEntity deliveryEntity = deliveryDomainService.createProcessDelivery(
                    order.getOrderId(),
                    order.getProductHubId(),
                    order.getNearHubId(),
                    order.getZipCode(),
                    order.getAddress1(),
                    order.getAddress2(),
                    hubShipperId
            );

            // 허브 기록 테이블에 기록하기
            deliveryHubHistoryService.createHubHistory(
                    deliveryEntity.getDeliveryId(),
                    deliveryEntity.getDepartureHubId(),
                    deliveryEntity.getDestinationHubId(),
                    deliveryEntity.getHubShipperId(),
                    deliveryEntity.getDeliveryStatus()
            );

            deliveryMessageService.processHubChangeStatus(deliveryEntity.getOrderId(), "HUB_DELIVERY_ASSIGNMENT_IN_PROGRESS");
        }
    }

    // 스케줄링 - 허브 배송 물건 상차 및 주문 상태값 변경, slack 메세지 발송 : 20시
    @Scheduled(cron = "0 0 20 * * *")
    public void sendToSlackHubDelivery() {
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_IN_PROGRESS);
        updateStatusDeliveryHub(deliveries, DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_COMPLETED);
    }

    // 스케줄링 - 허브 배송 출발 : 21시
    @Scheduled(cron = "0 0 21 * * *")
    public void hubDeliveryStart() {
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_COMPLETED);
        updateStatusDeliveryHub(deliveries, DeliveryStatusEnum.MOVING_TO_HUB);
    }

    // 스케줄링 - 고객 배송 물건 상차 및 slack 메세지 발송 : 06시
    @Scheduled(cron = "0 05 3 * * *")
    public void sendToSlackCustomerDelivery() {
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_IN_PROGRESS);
        updateStatusDeliveryUser(deliveries, DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED);
    }

    // 스케줄링 - 고객 배송 출발 : 08시
    @Scheduled(cron = "0 06 3 * * *")
    public void userDeliveryStart() {
        List<DeliveryEntity> deliveries = findByDeliveryStatus(DeliveryStatusEnum.CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED);
        updateStatusDeliveryUser(deliveries, DeliveryStatusEnum.MOVING_TO_CUSTOMER);
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
    }

    // 배송 상태값으로 찾기
    private List<DeliveryEntity> findByDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        return deliveryRepository.findAllByDeliveryStatus(deliveryStatus);
    }
}

