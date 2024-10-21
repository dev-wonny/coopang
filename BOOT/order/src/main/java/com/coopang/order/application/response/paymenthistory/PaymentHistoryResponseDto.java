package com.coopang.order.application.response.paymenthistory;

import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentHistoryResponseDto {

    private UUID paymentHistoryId;
    private UUID pgPaymentId;
    private UUID orderId;
    private String paymentMethod;
    private BigDecimal paymentPrice;
    private String paymentStatus;
    private boolean isDeleted;

    private PaymentHistoryResponseDto(
            UUID paymentHistoryId,
            UUID pgPaymentId,
            UUID orderId,
            String paymentMethod,
            BigDecimal paymentPrice,
            String paymentStatus,
            boolean isDeleted
            ) {
        this.paymentHistoryId = paymentHistoryId;
        this.pgPaymentId = pgPaymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
        this.isDeleted = isDeleted;
    }

    public static PaymentHistoryResponseDto fromPaymentHistory(
            PaymentHistoryEntity paymentHistory
    ){
        return new PaymentHistoryResponseDto(
                paymentHistory.getPaymentHistoryId(),
                paymentHistory.getPgPaymentId(),
                paymentHistory.getOrderId(),
                paymentHistory.getPaymentMethod().toString(),
                paymentHistory.getPaymentPrice(),
                paymentHistory.getPaymentStatus().toString(),
                paymentHistory.isDeleted()
        );
    }
}
