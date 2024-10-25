package com.coopang.apidata.application.user;

import com.coopang.coredata.user.enums.UserRoleEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPermissionValidator {

    /**
     * MASTER 또는 본인 여부를 확인하는 메소드
     *
     * @param userId       사용자 ID
     * @param userIdHeader 헤더에서 받은 사용자 ID (본인 여부 확인용)
     * @param roleHeader   요청한 사용자의 역할 (헤더에서 전달된 값)
     * @throws SecurityException 권한이 없을 경우 예외를 던짐
     */
    public static void validateMasterOrSelf(UUID userId, String userIdHeader, String roleHeader) {
        // MASTER 권한이 있거나, 본인인지 확인
        if (!UserRoleEnum.isMaster(roleHeader) && isNotSelf(userId, userIdHeader)) {
            throw new SecurityException("본인만 조회 가능 합니다");
        }
    }

    public static void validateSelf(UUID userId, String userIdHeader) {
        // 본인인지 확인
        if (isNotSelf(userId, userIdHeader)) {
            throw new SecurityException("본인만 조회 가능 합니다");
        }
    }

    private static boolean isNotSelf(UUID userId, String userIdHeader) {
        return !userIdHeader.equals(userId.toString());
    }
}