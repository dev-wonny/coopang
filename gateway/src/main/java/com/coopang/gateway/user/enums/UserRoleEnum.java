package com.coopang.gateway.user.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    MASTER(Authority.MASTER),
    HUB_MANAGER(Authority.HUB_MANAGER),
    COMPANY(Authority.COMPANY),
    SHIPPER_HUB(Authority.SHIPPER_HUB),
    SHIPPER_COMPANY(Authority.SHIPPER_COMPANY);


    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String MASTER = "ROLE_MASTER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String COMPANY = "ROLE_COMPANY";
        public static final String SHIPPER_HUB = "ROLE_SHIPPER_HUB";
        public static final String SHIPPER_COMPANY = "ROLE_SHIPPER_COMPANY";
    }


    public static UserRoleEnum getRoleEnum(String s) {
        for (UserRoleEnum role : values()) {
            if (s.equals(role.authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid authority: " + s);
    }

    /**
     * 주어진 권한이 MASTER인지 확인합니다.
     *
     * @param s 권한 문자열
     * @return true if the authority is MASTER; false otherwise
     */
    public static boolean isMaster(String s) {
        return MASTER.authority.equals(s);
    }


    /**
     * MASTER 가 아니면 권한 접근 에외를 발생시킵니다.
     *
     * @param role
     */
    public static void validateMaster(String role) {
        if (!isMaster(role)) {
//            throw new CustomAccessDeniedException("Access denied. User role is not MANAGER or MASTER.");
        }
    }
}