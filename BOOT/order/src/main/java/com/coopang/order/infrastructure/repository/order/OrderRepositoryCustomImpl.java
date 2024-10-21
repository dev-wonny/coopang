package com.coopang.order.infrastructure.repository.order;

import static com.coopang.order.domain.entity.order.QOrderEntity.orderEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class OrderRepositoryCustomImpl extends Querydsl4RepositorySupport implements OrderRepositoryCustom {

    public OrderRepositoryCustomImpl() {
        super(OrderEntity.class);
    }

    /*
    Todo : 주문 전체 조회
    1. CUSTOMER 용 만들기
    2. HUB_MANAGER 용 만들기
    3. COMPANY 용 만들기
    4. MASTER 용 만들기
     */
    @Override
    public Page<OrderEntity> findAllByUserId(UUID userId, OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(userIdEq(userId)
                                .and(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(userIdEq(userId)
                                .and(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate())))
        );
    }

    @Override
    public Page<OrderEntity> findAllByProductHubId(UUID hubId, OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(hubIdEq(hubId)
                                .and(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(hubIdEq(hubId)
                                .and(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate())))
        );
    }

    @Override
    public Page<OrderEntity> findAllByCompanyId(UUID companyId, OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(companyIdEq(companyId)
                                .and(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(companyIdEq(companyId)
                                .and(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate())))
        );
    }

    @Override
    public Page<OrderEntity> findAllByCondition(OrderGetAllConditionDto orderGetAllConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate())),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(orderDateBetween(orderGetAllConditionDto.getStartDate(), orderGetAllConditionDto.getEndDate()))
        );
    }

    /*
    Todo : 주문 검색
    1. CUSTOMER 용 만들기
    2. HUB_MANAGER 용 만들기
    3. COMPANY 용 만들기
    4. MASTER 용 만들기
     */
    @Override
    public Page<OrderEntity> SearchByUserId(UUID userId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(userIdEq(userId)
                                .and(orderIdEq(orderSearchConditionDto.getOrderId()))
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(userIdEq(userId)
                                .and(orderIdEq(orderSearchConditionDto.getOrderId()))
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate())))
        );
    }

    @Override
    public Page<OrderEntity> SearchByProductHubId(UUID hubId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(hubIdEq(hubId)
                                .and(orderIdEq(orderSearchConditionDto.getOrderId()))
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(hubIdEq(hubId)
                                .and(orderIdEq(orderSearchConditionDto.getOrderId()))
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate())))
        );
    }

    @Override
    public Page<OrderEntity> SearchByCompanyId(UUID companyId, OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(companyIdEq(companyId)
                                .and(orderIdEq(orderSearchConditionDto.getOrderId()))
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(companyIdEq(companyId)
                                .and(orderIdEq(orderSearchConditionDto.getOrderId()))
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate())))
        );
    }

    @Override
    public Page<OrderEntity> Search(OrderSearchConditionDto orderSearchConditionDto, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(orderEntity)
                        .where(orderIdEq(orderSearchConditionDto.getOrderId())
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(orderEntity)
                        .where(orderIdEq(orderSearchConditionDto.getOrderId())
                                .and(orderDateBetween(orderSearchConditionDto.getStartDate(), orderSearchConditionDto.getEndDate())))
        );
    }

    private BooleanExpression userIdEq(UUID userId) {
        return userId != null ? orderEntity.userId.eq(userId) : null;
    }

    private BooleanExpression hubIdEq(UUID hubId) {
        return hubId != null ? orderEntity.productHubId.eq(hubId) : null;
    }

    private BooleanExpression companyIdEq(UUID companyId) {
        return companyId != null ? orderEntity.companyId.eq(companyId) : null;
    }

    private BooleanExpression orderIdEq(UUID orderId) {
        return orderId != null ? orderEntity.orderId.eq(orderId) : null;
    }

    private BooleanExpression orderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return orderEntity.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return orderEntity.createdAt.goe(startDate); // 시작일만 있을 경우
        } else if (endDate != null) {
            return orderEntity.createdAt.loe(endDate); // 종료일만 있을 경우
        }
        return null; // 두 날짜가 모두 null인 경우
    }
}
