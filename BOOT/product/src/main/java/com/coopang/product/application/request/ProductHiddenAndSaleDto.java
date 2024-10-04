package com.coopang.product.application.request;

import lombok.Data;

@Data
public class ProductHiddenAndSaleDto {
    private boolean isHidden;
    private boolean isSale;

    public boolean getIsHidden(){
        return isHidden;
    }

    public boolean getIsSale(){
        return isSale;
    }
}
