package com.coopang.apidata.application.user.enums;

import com.coopang.apiconfig.error.AccessDeniedException;
import lombok.Getter;

import java.util.Set;

@Getter
public enum UserRoleEnum {
    MASTER(Authority.MASTER),
    HUB_MANAGER(Authority.HUB_MANAGER),
    COMPANY(Authority.COMPANY),
    SHIPPER(Authority.SHIPPER),
    CUSTOMER(Authority.CUSTOMER);


    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String MASTER = "ROLE_MASTER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String COMPANY = "ROLE_COMPANY";
        public static final String SHIPPER = "ROLE_SHIPPER";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
    }

    public static UserRoleEnum getRoleEnum(String s) {
        try {
            return UserRoleEnum.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authority: " + s);
        }
    }

    public static void validateRole(String role, Set<String> allowedRoles, String errorMessage) {
        if (!allowedRoles.contains(role)) {
            throw new AccessDeniedException(errorMessage);
        }
    }

    public static void validateMasterOrManager(String role) {
        validateRole(role, Set.of(Authority.MASTER, Authority.HUB_MANAGER),
                "Access denied. User role is not HUB_MANAGER or MASTER.");
    }

    public static void validateMasterOrManagerOrCompany(String role) {
        validateRole(role, Set.of(Authority.MASTER, Authority.HUB_MANAGER, Authority.COMPANY),
                "Access denied. User role is not HUB_MANAGER or MASTER or COMPANY.");
    }

    public static void validateCustomer(String role) {
        validateRole(role, Set.of(Authority.CUSTOMER),
                "Access denied. User role is not CUSTOMER.");
    }

    public static boolean isManagerOrMaster(String role) {
        return Set.of(Authority.MASTER, Authority.HUB_MANAGER).contains(role);
    }

    public static boolean isManagerMasterOrCompany(String role) {
        return Set.of(Authority.MASTER, Authority.HUB_MANAGER, Authority.COMPANY).contains(role);
    }

    public static boolean isMaster(String role) {
        return Authority.MASTER.equals(role);
    }

    public static boolean isCustomer(String role) {
        return Authority.CUSTOMER.equals(role);
    }
}