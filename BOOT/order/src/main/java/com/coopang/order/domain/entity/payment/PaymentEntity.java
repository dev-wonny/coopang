package com.coopang.order.domain.entity.payment;

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
public class PaymentEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "payment_id", columnDefinition = "UUID", unique = true, nullable = false)
    private UUID paymentId;

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
    private PaymentEntity(
            UUID paymentId,
            UUID orderId,
            PaymentMethodEnum paymentMethod,
            BigDecimal paymentPrice,
            PaymentStatusEnum paymentStatus
    ){
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
    }

    public static PaymentEntity create(
            UUID orderId,
            BigDecimal paymentPrice,
            PaymentStatusEnum paymentStatus
    ){
        return PaymentEntity.builder()
                .orderId(orderId)
                .paymentMethod(PaymentMethodEnum.CARD)
                .paymentPrice(paymentPrice)
                .paymentStatus(paymentStatus)
                .build();
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
