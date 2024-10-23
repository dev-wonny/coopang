package com.coopang.order.infrastructure.repository.paymenthistory;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import com.coopang.order.presentation.request.paymenthistory.PaymentHistorySearchConditionDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.coopang.order.domain.entity.order.QOrderEntity.orderEntity;
import static com.coopang.order.domain.entity.paymenthistory.QPaymentHistoryEntity.paymentHistoryEntity;

@Repository
public class PaymentHistoryRepositoryCustomImpl extends Querydsl4RepositorySupport implements PaymentHistoryRepositoryCustom {

    public PaymentHistoryRepositoryCustomImpl() {super(PaymentHistoryEntity.class);}

    @Override
    public Page<PaymentHistoryEntity> search(PaymentHistorySearchConditionDto searchCondition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(paymentHistoryEntity)
                        .where(orderIdEq(searchCondition.getOrderId())
                                .and(paymentHistoryDateBetween(searchCondition.getStartDate(), searchCondition.getEndDate()))),
                countQuery -> countQuery
                        .selectFrom(paymentHistoryEntity)
                        .where(orderIdEq(searchCondition.getOrderId())
                                .and(paymentHistoryDateBetween(searchCondition.getStartDate(), searchCondition.getEndDate())))
        );
    }

    @Override
    public Page<PaymentHistoryEntity> findAll(PaymentHistorySearchConditionDto searchCondition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(paymentHistoryEntity)
                        .where(paymentHistoryDateBetween(searchCondition.getStartDate(), searchCondition.getEndDate())),
                countQuery -> countQuery
                        .selectFrom(paymentHistoryEntity)
                        .where(paymentHistoryDateBetween(searchCondition.getStartDate(), searchCondition.getEndDate()))
        );
    }
    private BooleanExpression orderIdEq(UUID orderId) {
        return orderId != null ? orderEntity.orderId.eq(orderId) : null;
    }

    private BooleanExpression paymentHistoryDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return paymentHistoryEntity.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return paymentHistoryEntity.createdAt.goe(startDate); // 시작일만 있을 경우
        } else if (endDate != null) {
            return paymentHistoryEntity.createdAt.loe(endDate); // 종료일만 있을 경우
        }
        return null; // 두 날짜가 모두 null인 경우
    }
}
