package com.coopang.apidata.application.order.response;

import com.coopang.apidata.application.address.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private UUID productId;
    private UUID userId;
    private UUID nearHubId;
    private UUID productHubId;
    private Address address;
    private Integer orderQuantity;
    private BigDecimal orderSinglePrice;
    private BigDecimal orderTotalPrice;
    private String orderStatus;
    private boolean isDeleted;
}
