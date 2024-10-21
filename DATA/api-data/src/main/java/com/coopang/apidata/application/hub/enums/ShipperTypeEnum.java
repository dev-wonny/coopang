package com.coopang.apidata.application.hub.enums;

import lombok.Getter;

@Getter
public enum ShipperTypeEnum {
    SHIPPER_HUB(Authority.SHIPPER_HUB)
    , SHIPPER_CUSTOMER(Authority.SHIPPER_CUSTOMER)
    ;

    private final String authority;

    ShipperTypeEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String SHIPPER_HUB = "ROLE_SHIPPER_HUB";
        public static final String SHIPPER_CUSTOMER = "ROLE_SHIPPER_CUSTOMER";
    }

    public static ShipperTypeEnum getShipperTypeEnum(String s) {
        try {
            return ShipperTypeEnum.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type: " + s);
        }
    }
}