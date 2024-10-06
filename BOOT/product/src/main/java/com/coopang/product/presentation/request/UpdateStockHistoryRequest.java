package com.coopang.product.presentation.request;

import com.coopang.product.domain.entity.ProductStockHistoryChangeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStockHistoryRequest {

    @PositiveOrZero(message = "수량은 양수로 입력해야됩니다.")
    private int productStockHistoryChangeQuantity;
    @PositiveOrZero(message = "수량은 양수로 입력해야됩니다.")
    private int productStockHistoryPreviousQuantity;
    @PositiveOrZero(message = "수량은 양수로 입력해야됩니다.")
    private int productStockHistoryCurrentQuantity;
    @NotBlank
    private String productStockHistoryAdditionalInfo;
    @NotBlank
    private ProductStockHistoryChangeType productStockHistoryChangeType;
}
