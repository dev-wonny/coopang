package com.coopang.order.infrastructure.repository.order;

import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepositoryCustom {
    // 전체 조회
    Page<OrderEntity> findAllByUser(UUID userId, OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);
    Page<OrderEntity> findAllByHub(UUID hubId, OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);
    Page<OrderEntity> findAllByCompany(UUID companyId, OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);
    Page<OrderEntity> findAllByMaster(OrderGetAllConditionDto orderGetAllConditionDto,Pageable pageable);

    // 검색
    Page<OrderEntity> findAllByUserSearch(UUID userId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
    Page<OrderEntity> findAllByHubSearch(UUID hubId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
    Page<OrderEntity> findAllByCompanySearch(UUID companyId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
    Page<OrderEntity> findAllByMasterSearch(OrderSearchConditionDto orderSearchConditionDto, Pageable pageable);
}
