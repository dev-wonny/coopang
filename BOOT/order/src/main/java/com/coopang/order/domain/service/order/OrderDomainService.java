package com.coopang.order.domain.service.order;

import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.infrastructure.repository.order.OrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "OrderDomainService")
@Service
@Transactional
public class OrderDomainService {

    private final OrderJpaRepository orderJpaRepository;

    public OrderDomainService(
            OrderJpaRepository orderJpaRepository
    ) {
        this.orderJpaRepository = orderJpaRepository;
    }

    // 주문 생성
    public OrderEntity createOrder(UUID userId, OrderDto orderDto){
        OrderEntity orderEntity = OrderEntity.create(
                 userId
                , orderDto.getProductId()
                , orderDto.getZipCode()
                , orderDto.getAddress1()
                , orderDto.getAddress2()
                , orderDto.getOrderQuantity()
                , orderDto.getOrderSinglePrice()
                , orderDto.getOrderTotalPrice()
                , orderDto.getProductHubId()
                , orderDto.getNearHubId()
        );

        return orderJpaRepository.save(orderEntity);
    }

    public void validateOrderStatusUpdate(OrderStatusEnum currentStatus, OrderStatusEnum newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    private boolean isValidTransition(OrderStatusEnum currentStatus, OrderStatusEnum orderStatusEnum) {
        return switch (currentStatus) {
            case READY -> orderStatusEnum == OrderStatusEnum.PENDING;
            case PENDING -> orderStatusEnum == OrderStatusEnum.SHIPPED || orderStatusEnum == OrderStatusEnum.CANCELED;
            case SHIPPED -> orderStatusEnum == OrderStatusEnum.DELIVERED;
            case DELIVERED -> false; // Delivered 상태에서 더 이상 변경할 수 없음
            case CANCELED -> false; // Canceled 상태에서 더 이상 변경할 수 없음
            default -> false; // 유효하지 않은 상태
        };
    }

    public void validateOrderDelete(OrderStatusEnum orderStatus) {
        if (orderStatus != OrderStatusEnum.CANCELED && orderStatus != OrderStatusEnum.SHIPPED) {
            throw new IllegalArgumentException("Order cannot be deleted unless it is in CANCELED or SHIPPED status. Current status: " + orderStatus);
        }
    }

}
