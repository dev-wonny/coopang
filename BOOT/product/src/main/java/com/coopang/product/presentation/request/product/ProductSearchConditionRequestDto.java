package com.coopang.product.presentation.request.product;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductSearchConditionRequestDto extends ProductBaseSearchConditionRequestDto {

    private UUID productId;
    private String productName;
    private UUID companyId;
    private double minProductPrice;
    private double maxProductPrice;
    private String startDate;
    private String endDate;
    private boolean isAbleToWatchDeleted = false;

    public boolean getIsAbleToWatchDeleted(){
        return this.isAbleToWatchDeleted;
    }
}
