package com.coopang.delivery.infrastructure.messaging.delivery;

import com.coopang.apicommunication.kafka.message.CompleteDelivery;
import com.coopang.apicommunication.kafka.message.HubDeliveryNotification;
import com.coopang.apicommunication.kafka.message.ProcessChangeStatus;
import com.coopang.apicommunication.kafka.message.UserDeliveryNotification;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j(topic = "DeliveryKafkaProducer")
@Component
public class DeliveryKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public DeliveryKafkaProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    // Todo : 배송 상태 변경으로 인한 주문 상태 변화 필요시 보내는 producer
    /*
    <process_change_status>
    order_id
    delivery_status
     */
    public void processChangeStatus(DeliveryEntity deliveryEntity){
        try {
            ProcessChangeStatus processChangeStatus = new ProcessChangeStatus();
            processChangeStatus.setOrderId(deliveryEntity.getOrderId());
            processChangeStatus.setDeliveryStatus(deliveryEntity.getDeliveryStatus().toString());

            final String sendMessage = objectMapper.writeValueAsString(processChangeStatus);
            kafkaTemplate.send("process_change_status", sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Todo : slack쪽으로 메세지 정보 보내기 (허브 배송)
    /*
    <hub_delivery_notification>
    delivery_shipper_id
    message (배송 건 리스트에 담아서 보내기)
     */
    public void hubDeliveryNotification(List<DeliveryEntity> deliveries){
        try {
            StringBuilder messageBuilder = new StringBuilder();
            for (DeliveryEntity deliveryEntity : deliveries) {
                messageBuilder.append(deliveryEntity.toString()).append("\n"); // 각 배송 정보를 줄 바꿈으로 구분
            }

            HubDeliveryNotification hubDeliveryNotification = new HubDeliveryNotification();
            hubDeliveryNotification.setDeliveryShipperId(deliveries.get(0).getHubShipperId());
            hubDeliveryNotification.setMessage(messageBuilder.toString());

            final String sendMessage = objectMapper.writeValueAsString(hubDeliveryNotification);
            kafkaTemplate.send("hub_delivery_notification", sendMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Todo : slack쪽으로 메세지 정보 보내기 (고객 배송)
    /*
    <user_delivery_notification>
    delivery_shipper_id
    message (배송 건 리스트에 담아서 보내기)
     */
    public void userDeliveryNotification(List<DeliveryEntity> deliveries){
        try {
            StringBuilder messageBuilder = new StringBuilder();
            for (DeliveryEntity deliveryEntity : deliveries) {
                messageBuilder.append(deliveryEntity.toString()).append("\n"); // 각 배송 정보를 줄 바꿈으로 구분
            }

            UserDeliveryNotification userDeliveryNotification = new UserDeliveryNotification();
            userDeliveryNotification.setDeliveryShipperId(deliveries.get(0).getUserShipperId());
            userDeliveryNotification.setMessage(messageBuilder.toString());

            final String sendMessage = objectMapper.writeValueAsString(userDeliveryNotification);
            kafkaTemplate.send("user_delivery_notification", sendMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
