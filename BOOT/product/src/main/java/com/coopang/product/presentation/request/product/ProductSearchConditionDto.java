package com.coopang.product.presentation.request.product;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductSearchConditionDto extends ProductBaseSearchConditionDto {

    private String productName;
    private double minProductPrice;
    private double maxProductPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isAbleToWatchDeleted = false;

    public void setIsAbleToWatchDeleted(boolean isAbleToWatchDeleted) {
        this.isAbleToWatchDeleted = isAbleToWatchDeleted;
    }

    public boolean getIsAbleToWatchDeleted(){
        return this.isAbleToWatchDeleted;
    }
}
