package com.coopang.order.domain.service;

import com.coopang.order.application.ProcessDelivery;
import com.coopang.order.application.request.OrderDto;
import com.coopang.order.domain.OrderStatusEnum;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.infrastructure.repository.OrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "OrderDomainService")
@Service
@Transactional
public class OrderDomainService {

    private final OrderJpaRepository orderJpaRepository;

    public OrderDomainService(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

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

    public ProcessDelivery processDelivery(
            UUID orderId,
            UUID userId,
            UUID companyId,
            String zipCode,
            String address1,
            String address2
    ){
        ProcessDelivery message = new ProcessDelivery();
        message.setOrderId(orderId);
        message.setUserId(userId);
        message.setCompanyId(companyId);
        message.setZipCode(zipCode);
        message.setAddress1(address1);
        message.setAddress2(address2);
        return message;
    }

}
