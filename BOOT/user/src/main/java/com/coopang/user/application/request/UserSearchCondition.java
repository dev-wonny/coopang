package com.coopang.user.application.request;

import com.coopang.coredata.user.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class UserSearchCondition {
    private UUID userId;
    private String userName;
    private UserRoleEnum userRole;
    private String email;
    private Boolean isDeleted;

    @Builder
    private UserSearchCondition(UUID userId, String userName, UserRoleEnum userRole, String email, Boolean isDeleted) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.email = email;
        this.isDeleted = isDeleted;
    }

    public static UserSearchCondition from(UUID userId, String userName, String userRole, String email, Boolean isDeleted) {
        return UserSearchCondition.builder()
            .userId(userId)
            .userName(userName)
            .userRole(userRole != null ? UserRoleEnum.getRoleEnum(userRole) : null)
            .email(email)
            .isDeleted(isDeleted != null ? isDeleted : false)
            .build();
    }

    /**
     * redis 캐싱 조건이라 equals, hashCode 추가
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // 동일한 객체일 경우 true 반환
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // 클래스 타입이 다를 경우 false 반환
        }
        UserSearchCondition that = (UserSearchCondition) o; // 비교 대상 캐스팅
        return Objects.equals(userId, that.userId) &&
            Objects.equals(userName, that.userName) &&
            userRole == that.userRole &&
            Objects.equals(email, that.email) &&
            Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userRole, email, isDeleted); // 객체 필드를 기반으로 해시코드 생성
    }
}