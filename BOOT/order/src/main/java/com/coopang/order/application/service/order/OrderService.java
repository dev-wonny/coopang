package com.coopang.order.application.service.order;

import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.order.OrderRepository;
import com.coopang.order.domain.service.order.OrderDomainService;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "OrderService")
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;

    public OrderService(OrderRepository orderRepository, OrderDomainService orderDomainService) {
        this.orderRepository = orderRepository;
        this.orderDomainService = orderDomainService;
    }

    // 주문 등록
    public OrderResponseDto createOrder(UUID userId, OrderDto orderDto) {
        // 주문 등록
        OrderEntity orderEntity = orderDomainService.createOrder(userId, orderDto);
        log.info("Order created successfully: {}", orderEntity);
        return OrderResponseDto.fromOrder(orderEntity);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findById(UUID orderId) {
        OrderEntity orderEntity = findByOrderId(orderId);
        return OrderResponseDto.fromOrder(orderEntity);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findCompanyById(UUID companyId, UUID orderId) {
        return findOrderByEntityId(companyId, orderId, "COMPANY");
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findHubById(UUID hubId, UUID orderId) {
        return findOrderByEntityId(hubId, orderId, "HUB");
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findUserById(UUID userId, UUID orderId) {
        return findOrderByEntityId(userId, orderId, "CUSTOMER");
    }

    /*
    주문 전체 조회
    1. CUSTOMER 용 만들기
    2. HUB_MANAGER 용 만들기
    3. COMPANY 용 만들기
    4. MASTER 용 만들기
     */
    // 1. CUSTOMER 용 만들기
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByUser(UUID userId, OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAllByUserId(userId, orderGetAllConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    // 2. HUB_MANAGER 용 만들기
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByHub(UUID hubId, OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAllByProductHubId(hubId, orderGetAllConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    // 3. COMPANY 용 만들기
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByCompany(UUID companyId, OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAllByCompanyId(companyId, orderGetAllConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    // 4. MASTER 용 만들기
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByMaster(OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAllByCondition(orderGetAllConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    /*
    주문 검색
    1. CUSTOMER 용 만들기
    2. HUB_MANAGER 용 만들기
    3. COMPANY 용 만들기
    4. MASTER 용 만들기
    * RequestParam startDate, endDate 생성하기
    * keyword 는 우선 orderId
     */
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByUserSearch(UUID userId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.SearchByUserId(userId, orderSearchConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByHubSearch(UUID hubId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.SearchByProductHubId(hubId, orderSearchConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByCompanySearch(UUID companyId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.SearchByCompanyId(companyId, orderSearchConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAllByMasterSearch(OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.Search(orderSearchConditionDto, pageable);
        return orders.map(OrderResponseDto::fromOrder);
    }

    /*
    주문 수정
     */
    public void updateOrder(UUID orderId, OrderDto orderDto) {
        OrderEntity orderEntity = findByOrderId(orderId);
        orderEntity.updateOrder(orderDto.getNearHubId(), orderDto.getZipCode(), orderDto.getAddress1(), orderDto.getAddress2());
        log.info("Order updated successfully: {}", orderId);
    }

    // 주문 상태 update
    public void updateOrderStatus(UUID orderId, OrderStatusEnum orderStatusEnum) {
        OrderEntity orderEntity = findByOrderId(orderId);
        // 검증 : 변화하기 전 상태값이 올바른지 확인
        orderDomainService.validateOrderStatusUpdate(orderEntity.getOrderStatus(), orderStatusEnum);
        // 상태 업데이트 로직
        orderEntity.setOrderStatus(orderStatusEnum);
    }

    public void updateOrderStatusByHub(UUID hubId, UUID orderId, OrderStatusEnum orderStatusEnum) {
        OrderEntity orderEntity = findByOrderId(orderId);
        if (orderEntity.getProductHubId().equals(hubId)) {
            orderDomainService.validateOrderStatusUpdate(orderEntity.getOrderStatus(), orderStatusEnum);
            orderEntity.setOrderStatus(orderStatusEnum);
        } else {
            throw new IllegalArgumentException("you cannot change");
        }
    }

    public void updateOrderStatusByCustomer(UUID userId, UUID orderId, OrderStatusEnum orderStatusEnum) {
        OrderEntity orderEntity = findByOrderId(orderId);
        if (orderEntity.getUserId().equals(userId)) {
            orderDomainService.validateOrderStatusUpdate(orderEntity.getOrderStatus(), orderStatusEnum);
            orderEntity.setOrderStatus(orderStatusEnum);
        } else {
            throw new IllegalArgumentException("you cannot change");
        }
    }

    /*
    주문 삭제
    * 주문 상태가 Canceled / Shipped 일 경우에만 가능
     */
    public void deleteOrder(UUID orderId) {
        OrderEntity orderEntity = findByOrderId(orderId);
        // 검증 주문 상태가 Canceled / Shipped 일 경우에만 가능
        orderDomainService.validateOrderDelete(orderEntity.getOrderStatus());
        orderEntity.setDeleted(true);
        log.debug("deleted order: {}", orderEntity);
    }

    public void deleteOrderByHubManager(UUID hubId, UUID orderId) {
        findOrderByEntityId(hubId, orderId, "HUB");
    }

    /*
    feginclient
     */
    public List<OrderResponseDto> getOrderList() {
        List<OrderEntity> orderEntity = orderRepository.findOrderList(OrderStatusEnum.PENDING);
        return orderEntity.stream().map(OrderResponseDto::fromOrder).toList();
    }

    // 공통 메서드
    private OrderEntity findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .filter(o -> !o.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Order not found. orderId=" + orderId));
    }

    private OrderResponseDto findOrderByEntityId(UUID entityId, UUID orderId, String entityType) {
        OrderEntity orderEntity = findByOrderId(orderId);
        // 주문이 존재하지 않으면 예외 처리
        if (orderEntity == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }

        boolean isMatch = switch (entityType) {
            case "COMPANY" -> orderEntity.getCompanyId().equals(entityId);
            case "HUB" -> orderEntity.getProductHubId().equals(entityId);
            case "CUSTOMER" -> orderEntity.getUserId().equals(entityId);
            default -> throw new IllegalArgumentException("Invalid entity type.");
        };

        if (isMatch) {
            return OrderResponseDto.fromOrder(orderEntity);
        } else {
            throw new IllegalArgumentException(entityType + " ID does not match the order's corresponding ID.");
        }
    }
}
