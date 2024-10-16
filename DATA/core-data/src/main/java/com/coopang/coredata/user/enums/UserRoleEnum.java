package com.coopang.coredata.user.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum UserRoleEnum {//역할
    MASTER(Authority.MASTER)
    , HUB_MANAGER(Authority.HUB_MANAGER)
    , COMPANY(Authority.COMPANY)
    , SHIPPER(Authority.SHIPPER)
    , CUSTOMER(Authority.CUSTOMER)
    , SERVER(Authority.SERVER)
    ;

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {//권한
        public static final String MASTER = "ROLE_MASTER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String COMPANY = "ROLE_COMPANY";
        public static final String SHIPPER = "ROLE_SHIPPER";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String SERVER = "ROLE_SERVER";
    }

    public static UserRoleEnum getRoleEnum(String s) {
        try {
            return UserRoleEnum.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + s);
        }
    }

    public static void validateRole(UserRoleEnum role, Set<UserRoleEnum> allowedRoles, String errorMessage) {
        if (!allowedRoles.contains(role)) {
            throw new SecurityException(errorMessage);
        }
    }

    public static void validateMasterOrManager(UserRoleEnum role) {
        validateRole(role, Set.of(MASTER, HUB_MANAGER), "Access denied. User role is not HUB_MANAGER or MASTER.");
    }

    public static void validateMasterOrManagerOrCompany(UserRoleEnum role) {
        validateRole(role, Set.of(MASTER, HUB_MANAGER, COMPANY), "Access denied. User role is not HUB_MANAGER or MASTER or COMPANY.");
    }

    public static void validateCustomer(UserRoleEnum role) {
        validateRole(role, Set.of(CUSTOMER), "Access denied. User role is not CUSTOMER.");
    }

    public static boolean isManagerOrMaster(String role) {
        return Set.of(MASTER, HUB_MANAGER).contains(getRoleEnum(role));
    }

    public static boolean isManagerMasterOrCompany(UserRoleEnum role) {
        return Set.of(MASTER, HUB_MANAGER, COMPANY).contains(role);
    }

    public static boolean isMaster(String role) {
        return MASTER.equals(getRoleEnum(role));
    }

    public static boolean isHubManager(String role) {
        return HUB_MANAGER.equals(getRoleEnum(role));
    }

    public static boolean isCompany(String role) {
        return COMPANY.equals(getRoleEnum(role));
    }

    public static boolean isShipper(String role) {
        return SHIPPER.equals(getRoleEnum(role));
    }

    public static boolean isCustomer(String role) {
        return CUSTOMER.equals(getRoleEnum(role));
    }

    public static boolean isServer(String role) {
        return SERVER.equals(getRoleEnum(role));
    }
}