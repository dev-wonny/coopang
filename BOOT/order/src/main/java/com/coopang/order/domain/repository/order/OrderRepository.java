package com.coopang.order.domain.repository.order;

import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<OrderEntity> findById(UUID orderId);

    // 전체 조회
    Page<OrderEntity> findAllByUserId(UUID userId, OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);
    Page<OrderEntity> findAllByProductHubId(UUID hubId, OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);
    Page<OrderEntity> findAllByCompanyId(UUID companyId, OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);
    Page<OrderEntity> findAllByCondition(OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);

    // 검색
    Page<OrderEntity> SearchByUserId(UUID userId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
    Page<OrderEntity> SearchByProductHubId(UUID hubId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
    Page<OrderEntity> SearchByCompanyId(UUID companyId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
    Page<OrderEntity> Search(OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);

}
