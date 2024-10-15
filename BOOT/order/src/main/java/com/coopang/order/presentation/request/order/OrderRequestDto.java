package com.coopang.order.presentation.request.order;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderRequestDto {

    /*
    제품의 대한 정보
     */
    private UUID productId;
    private UUID productHubId;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
    /*
    배송지에 대한 정보
     */
    private String zipCode;
    private String address1;
    private String address2;
    private UUID nearHubId;
    /*
    결제 방법에 대한 정보
     */
    private String paymentMethod;
    private UUID pgPaymentId;
}
