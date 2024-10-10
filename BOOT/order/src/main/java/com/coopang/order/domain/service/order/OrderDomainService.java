package com.coopang.order.domain.service.order;

import com.coopang.apicommunication.kafka.message.ProcessDelivery;
import com.coopang.apicommunication.kafka.message.ProcessPayment;
import com.coopang.apicommunication.kafka.message.ProcessProduct;
import com.coopang.apicommunication.kafka.message.RollbackProduct;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.infrastructure.messaging.order.OrderKafkaProducer;
import com.coopang.order.infrastructure.repository.order.OrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j(topic = "OrderDomainService")
@Service
@Transactional
public class OrderDomainService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderKafkaProducer orderKafkaProducer;

    public OrderDomainService(
            OrderJpaRepository orderJpaRepository,
            OrderKafkaProducer orderKafkaProducer
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderKafkaProducer = orderKafkaProducer;
    }

    // 주문 생성
    public OrderEntity createOrder(OrderDto orderDto){
        OrderEntity orderEntity = OrderEntity.create(
                UUID.randomUUID(), // 임시로 랜덤 원래는 사용자 ID 넣기
                orderDto.getProductId(),
                orderDto.getZipCode(),
                orderDto.getAddress1(),
                orderDto.getAddress2(),
                orderDto.getOrderQuantity(),
                orderDto.getOrderSinglePrice(),
                orderDto.getOrderTotalPrice()
        );

        return orderJpaRepository.save(orderEntity);
    }

    // process_product 로 전달
    public void sendProcessProduct(UUID orderId){
        OrderEntity orderEntity = findByOrderId(orderId);

        ProcessProduct processProduct = new ProcessProduct();
        processProduct.setOrderId(orderEntity.getOrderId());
        processProduct.setProductId(orderEntity.getProductId());
        processProduct.setOrderQuantity(orderEntity.getOrderQuantity());
        processProduct.setOrderTotalPrice(orderEntity.getOrderTotalPrice());

        orderKafkaProducer.sendProcessProduct(processProduct);
    }

    // process_delivery 로 전달
    public void sendProcessDelivery(UUID orderId){
        OrderEntity orderEntity = findByOrderId(orderId);

        ProcessDelivery processDelivery = new ProcessDelivery();
        processDelivery.setOrderId(orderEntity.getOrderId());
        processDelivery.setUserId(orderEntity.getUserId());
        processDelivery.setCompanyId(orderEntity.getCompanyId());
        processDelivery.setZipCode(orderEntity.getAddressEntity().getZipCode());
        processDelivery.setAddress1(orderEntity.getAddressEntity().getAddress1());
        processDelivery.setAddress2(orderEntity.getAddressEntity().getAddress2());

        orderKafkaProducer.sendProcessDelivery(processDelivery);
    }

    // process_payment 로 전달
    public void sendProcessPayment(UUID orderId, String message){
        OrderEntity orderEntity = findByOrderId(orderId);

        ProcessPayment processPayment = new ProcessPayment();
        processPayment.setOrderId(orderEntity.getOrderId());
        processPayment.setProductId(orderEntity.getProductId());
        processPayment.setOrderQuantity(orderEntity.getOrderQuantity());
        processPayment.setOrderTotalPrice(orderEntity.getOrderTotalPrice());
        processPayment.setMessage(message);

        orderKafkaProducer.sendProcessPayment(processPayment);
    }

    // rollback_product 로 전달
    public void sendRollbackProduct(UUID orderId){
        OrderEntity orderEntity = findByOrderId(orderId);

        RollbackProduct rollbackProduct = new RollbackProduct();
        rollbackProduct.setOrderId(orderEntity.getOrderId());
        rollbackProduct.setProductId(orderEntity.getProductId());
        rollbackProduct.setOrderQuantity(orderEntity.getOrderQuantity());
        rollbackProduct.setOrderTotalPrice(orderEntity.getOrderTotalPrice());

        orderKafkaProducer.sendRollbackProduct(rollbackProduct);
    }

    private OrderEntity findByOrderId(UUID orderId) {
        return orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found. orderId=" + orderId));
    }

}
