package com.coopang.order.domain.entity.paymenthistory;

import com.coopang.apidata.application.payment.enums.PaymentMethodEnum;
import com.coopang.apidata.application.payment.enums.PaymentStatusEnum;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PaymentHistoryEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "payment_history_id", columnDefinition = "UUID", unique = true, nullable = false)
    private UUID paymentHistoryId;

    @Column(name = "pg_payment_id", columnDefinition = "UUID", nullable = false)
    private UUID pgPaymentId;

    @Column(name = "order_id", columnDefinition = "UUID", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "payment_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal paymentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatusEnum paymentStatus;

    @Builder
    private PaymentHistoryEntity(
            UUID paymentHistoryId,
            UUID pgPaymentId,
            UUID orderId,
            PaymentMethodEnum paymentMethod,
            BigDecimal paymentPrice,
            PaymentStatusEnum paymentStatus
    ){
        this.paymentHistoryId = paymentHistoryId;
        this.pgPaymentId = pgPaymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
    }

    public static PaymentHistoryEntity create(
            UUID orderId,
            UUID pgPaymentId,
            PaymentMethodEnum paymentMethod,
            BigDecimal paymentPrice
    ){
        return PaymentHistoryEntity.builder()
                .orderId(orderId)
                .pgPaymentId(pgPaymentId)
                .paymentMethod(paymentMethod)
                .paymentPrice(paymentPrice)
                .paymentStatus(PaymentStatusEnum.COMPLETED)
                .build();
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
