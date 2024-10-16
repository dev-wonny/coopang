package com.coopang.hub.application.validator;

import com.coopang.apiconfig.error.AccessDeniedException;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.hub.application.response.hub.HubResponseDto;
import com.coopang.hub.application.service.hub.HubService;

import java.util.UUID;

public class HubPermissionValidator {
    // todo AOP 변경

    /**
     * 허브 매니저 권한 확인 & 소속 허브 검증
     *
     * @param userRole
     * @param targetHubId
     * @param hubManagerId
     * @param hubService
     */
    public static void validateHubManagerBelongToHub(String userRole, UUID targetHubId, UUID hubManagerId, HubService hubService) {
        if (UserRoleEnum.isHubManager(userRole)) {
            verifyHubOfHubManager(targetHubId, hubManagerId, hubService);
        }
    }

    /**
     * 소속 허브 확인
     *
     * @param targetHubId  대상의 소속 hubId(company, shipper)
     * @param hubManagerId 허브 매니저 userId
     * @param hubService   허브 서비스
     * @throws AccessDeniedException targetHubId와 hubManager의 hubId가 일치하지 않으면 에러
     */
    public static void verifyHubOfHubManager(UUID targetHubId, UUID hubManagerId, HubService hubService) {
        final HubResponseDto targetHub = hubService.getHubById(targetHubId);
        if (!targetHub.getHubManagerId().equals(hubManagerId)) {
            throw new AccessDeniedException(
                    String.format("소속 허브만 가능합니다. targetHubId: %s, hubManagerId: %s", targetHubId, hubManagerId)
            );
        }
    }
}