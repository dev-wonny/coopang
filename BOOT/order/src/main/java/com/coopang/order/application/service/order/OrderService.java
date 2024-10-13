package com.coopang.order.application.service.order;

import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.application.service.message.OrderMessageService;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.order.OrderRepository;
import com.coopang.order.domain.service.order.OrderDomainService;
import com.coopang.order.infrastructure.message.MessageSender;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "OrderService")
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    private final OrderMessageService orderMessageService;

    public OrderService(OrderRepository orderRepository,
                        OrderDomainService orderDomainService,
                        OrderMessageService orderMessageService
    ) {
        this.orderRepository = orderRepository;
        this.orderDomainService = orderDomainService;
        this.orderMessageService = orderMessageService;
    }

    // 주문 등록
    public OrderResponseDto createOrder(OrderDto orderDto) {
        // 주문 등록
        OrderEntity orderEntity = orderDomainService.createOrder(orderDto);
        log.info("Order created successfully: {}", orderEntity);
        return OrderResponseDto.fromOrder(orderEntity);
    }

    /*
    Todo : 주문 단건 조회
     */
    public OrderResponseDto findById(UUID orderId) {
        OrderEntity orderEntity = findByOrderId(orderId);
        return OrderResponseDto.fromOrder(orderEntity);
    }

    /*
    Todo : 주문 전체 조회
    1. CUSTOMER 용 만들기
    2. HUB_MANAGER 용 만들기
    3. COMPANY 용 만들기
    4. MASTER 용 만들기
     */
    // 1. CUSTOMER 용 만들기
    public Page<OrderResponseDto> findAllByUser(
            UUID userId,
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByUser(userId,orderGetAllConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }
    // 2. HUB_MANAGER 용 만들기
    public Page<OrderResponseDto> findAllByHub(
            UUID hubId,
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByHub(hubId,orderGetAllConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }
    // 3. COMPANY 용 만들기
    public Page<OrderResponseDto> findAllByCompany(
            UUID companyId,
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByCompany(companyId,orderGetAllConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }
    // 4. MASTER 용 만들기
    public Page<OrderResponseDto> findAllByMaster(
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByMaster(orderGetAllConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    /*
    Todo : 주문 검색
    1. CUSTOMER 용 만들기
    2. HUB_MANAGER 용 만들기
    3. COMPANY 용 만들기
    4. MASTER 용 만들기
    * RequestParam startDate, endDate 생성하기
    * keyword 는 우선 orderId
     */

    public Page<OrderResponseDto> findAllByUserSearch(
            UUID userId,
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByUserSearch(userId,orderSearchConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    public Page<OrderResponseDto> findAllByHubSearch(
            UUID hubId,
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByHubSearch(hubId,orderSearchConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    public Page<OrderResponseDto> findAllByCompanySearch(
            UUID companyId,
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByCompanySearch(companyId,orderSearchConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    public Page<OrderResponseDto> findAllByMasterSearch(
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ){
        Page<OrderEntity> orders = orderRepository.findAllByMasterSearch(orderSearchConditionDto,pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    // 주문 상태 update
    public void updateOrderStatus(UUID orderId, OrderStatusEnum orderStatusEnum) {
        OrderEntity orderEntity = findByOrderId(orderId);
        orderEntity.setOrderStatus(orderStatusEnum);
    }

    /*
    주문 상태 Update (2)
    - Delivery 쪽에서 배송이 등록 되고 허브 정보들 가져올때
    - near_hub_id
    - hub_id
    - 채워주기
     */
    public void updateOrderInfo(
            UUID orderId,
            UUID productHubId,
            UUID nearHubId
    ) {
        OrderEntity orderEntity = findByOrderId(orderId);
        orderEntity.updateAtCompleteDelivery(productHubId, nearHubId);
    }

    // 공통 메서드
    private OrderEntity findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found. orderId=" + orderId));
    }
}
