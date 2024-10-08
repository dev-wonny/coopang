package com.coopang.order.application.response;

import com.coopang.order.domain.entity.payment.PaymentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentResponseDto {

    private UUID paymentId;
    private UUID orderId;
    private String paymentMethod;
    private BigDecimal paymentPrice;
    private String paymentStatus;
    private boolean isDeleted;

    private PaymentResponseDto(
            UUID paymentId,
            UUID orderId,
            String paymentMethod,
            BigDecimal paymentPrice,
            String paymentStatus,
            boolean isDeleted
            ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
        this.isDeleted = isDeleted;
    }

    public static PaymentResponseDto fromPayment(
            PaymentEntity payment
    ){
        return new PaymentResponseDto(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getPaymentMethod().toString(),
                payment.getPaymentPrice(),
                payment.getPaymentStatus().toString(),
                payment.isDeleted()
        );
    }
}
