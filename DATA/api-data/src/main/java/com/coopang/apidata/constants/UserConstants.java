package com.coopang.apidata.constants;

public class UserConstants {
    public static final String COOPANG_UPPERCASE = "COOPANG";
    public static final String COOPANG_LOWERCASE = "coopang";
    public static final String COOPANG_EMAIL = "@coopang.com";

    /**
     * apiconfig 의존성이 없는 경우 사용
     * com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ID 우선한다
     */
    public static final String HEADER_USER_ID = "X-User-Id";

    /**
     * apiconfig 의존성이 없는 경우 사용
     * com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ROLE 우선한다
     */
    public static final String HEADER_USER_ROLE = "X-User-Role";
}