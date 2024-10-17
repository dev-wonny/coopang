package com.coopang.delivery.application.service.message.delivery;

import com.coopang.apicommunication.kafka.consumer.MessageService;
import com.coopang.apicommunication.kafka.message.*;
import com.coopang.apicommunication.kafka.producer.MessageProducer;
import com.coopang.delivery.application.response.delivery.DeliveryResponseDto;
import com.coopang.delivery.application.service.delivery.DeliveryService;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.domain.service.delivery.DeliveryDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "DeliveryMessageService")
@Service
@Transactional
public class DeliveryMessageService implements MessageService {

    private final ObjectMapper objectMapper;
    private final MessageProducer messageProducer;
    private final DeliveryDomainService deliveryDomainService;
    private final DeliveryService deliveryService;

    public DeliveryMessageService(
            ObjectMapper objectMapper,
            MessageProducer messageProducer,
            DeliveryDomainService deliveryDomainService,
            DeliveryService deliveryService
    ) {
        this.objectMapper = objectMapper;
        this.messageProducer = messageProducer;
        this.deliveryDomainService = deliveryDomainService;
        this.deliveryService = deliveryService;
    }

    @Override
    public void processMessage(String topic, String message){
        log.info("Processing delivery topic {} message {}", topic, message);
        switch (topic) {
            case "complete_delivery":
                handleCompleteDelivery(message);
                break;
            case "cancel_delivery":
                handleCancelDelivery(message);
                break;
            default:
                log.warn("Unknown topic {}", topic);
        }
    }

    private void handleCompleteDelivery(String message){
        try {
            ProcessDelivery processDelivery = objectMapper.readValue(message, ProcessDelivery.class);
            deliveryService.processCreateDelivery(processDelivery);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleCancelDelivery(String message){
        try {
            CancelDelivery cancelDelivery = objectMapper.readValue(message, CancelDelivery.class);
            deliveryDomainService.cancelDelivery(cancelDelivery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     배송 상태 변경으로 인한 주문 상태 변화 필요시 보내는 producer
    <process_change_status>
    order_id
    delivery_status
     */
    public void processUserChangeStatus(UUID deliveryId){
        DeliveryResponseDto deliveryResponseDto = deliveryService.getDeliveryById(deliveryId);

        ProcessChangeStatus processChangeStatus = new ProcessChangeStatus();
        processChangeStatus.setOrderId(deliveryResponseDto.getOrderId());
        processChangeStatus.setDeliveryStatus(deliveryResponseDto.getDeliveryStatus().toString());

        sendMessage("process_change_status", processChangeStatus);
    }

    public void processHubChangeStatus(UUID orderId,String deliveryStatus){
        ProcessChangeStatus processChangeStatus = new ProcessChangeStatus();
        processChangeStatus.setOrderId(orderId);
        processChangeStatus.setDeliveryStatus(deliveryStatus);

        sendMessage("process_change_status", processChangeStatus);
    }
    /*
     slack 쪽으로 메세지 정보 보내기 (허브 배송)
    <hub_delivery_notification>
    delivery_shipper_id
    message (배송 건 리스트에 담아서 보내기)
     */
    public void hubDeliveryNotification(List<DeliveryEntity> deliveries){
        StringBuilder messageBuilder = new StringBuilder();
        for (DeliveryEntity deliveryEntity : deliveries) {
            messageBuilder.append(deliveryEntity.toString()).append("\n"); // 각 배송 정보를 줄 바꿈으로 구분
        }

        HubDeliveryNotification hubDeliveryNotification = new HubDeliveryNotification();
        hubDeliveryNotification.setDeliveryShipperId(deliveries.get(0).getHubShipperId());
        hubDeliveryNotification.setMessage(messageBuilder.toString());

        sendMessage("hub_delivery_notification", hubDeliveryNotification);
    }

    /*
     slack쪽으로 메세지 정보 보내기 (고객 배송)
    <user_delivery_notification>
    delivery_shipper_id
    message (배송 건 리스트에 담아서 보내기)
     */
    public void userDeliveryNotification(List<DeliveryEntity> deliveries){

        StringBuilder messageBuilder = new StringBuilder();
        for (DeliveryEntity deliveryEntity : deliveries) {
            messageBuilder.append(deliveryEntity.toString()).append("\n"); // 각 배송 정보를 줄 바꿈으로 구분
        }

        UserDeliveryNotification userDeliveryNotification = new UserDeliveryNotification();
        userDeliveryNotification.setDeliveryShipperId(deliveries.get(0).getUserShipperId());
        userDeliveryNotification.setMessage(messageBuilder.toString());

        sendMessage("user_delivery_notification", userDeliveryNotification);
    }

    /*
    공통 메서드
    1. 메세지 보내기
     */

    //1. 메세지 보내기
    private void sendMessage(String topic, Object payload) {
        try {
            final String message = objectMapper.writeValueAsString(payload);
            messageProducer.sendMessage(topic, message);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}: {}", topic, e.getMessage(), e);
        }
    }
}
