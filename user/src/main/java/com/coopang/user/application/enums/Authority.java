package com.coopang.user.application.enums;

public final class Authority {
    public static final String MASTER = "MASTER";
    public static final String HUB_MANAGER = "HUB_MANAGER";
    public static final String COMPANY = "COMPANY";
    public static final String SHIPPER_HUB = "SHIPPER_HUB";
    public static final String SHIPPER_COMPANY = "SHIPPER_COMPANY";

    private Authority() {
        // 상수만 제공하므로 인스턴스 생성을 방지합니다.
    }
}