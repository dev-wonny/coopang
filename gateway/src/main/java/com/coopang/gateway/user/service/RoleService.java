package com.coopang.gateway.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j(topic = "RoleService")
@Service
public class RoleService {
    private final RedisService redisService;

    public RoleService(RedisService redisService) {
        this.redisService = redisService;
    }

    public boolean checkUserRole(String userId, String role) {

        // 권한 validation
//        if (!UserRoleEnum.isValidRole(role)) {
//            log.warn("Invalid role: {}", role);
//            return false;
//        }
//
//        // CUSTOMER 는 role 체크 안한다
//        if (Objects.equals(role, UserRoleEnum.CUSTOMER.getAuthority())) {
//            return true;
//        }

        // OWNER, MANAGER, MASTER 레디스 role 체크
        return redisService.getRoleFromRedis(userId, role).isPresent();
    }

    public Mono<Boolean> checkUserRoleMono(String userId, String role) {

        // 권한 validation
//        if (!UserRoleEnum.isValidRole(role)) {
//            log.warn("Invalid role: {}", role);
//            return Mono.just(false);  // 비동기적으로 false 반환
//        }
//
//        // CUSTOMER 는 role 체크 안한다
//        if (Objects.equals(role, UserRoleEnum.CUSTOMER.getAuthority())) {
//            return Mono.just(true);  // 비동기적으로 true 반환
//        }
//
//        // OWNER, MANAGER, MASTER 레디스 role 체크
//        return redisService.getRoleFromRedisMono(userId, role)
//                .map(storedRole -> {
//                    boolean hasRole = storedRole.equals(role);
//                    if (!hasRole) {
//                        log.warn("Permission denied for user: {} with role: {}", userId, role);
//                    }
//                    return hasRole;
//                })
//                .defaultIfEmpty(false)  // 값이 없을 경우 false 반환
//                .onErrorResume(e -> {
//                    log.error("Error checking user role", e);
//                    return Mono.just(false);  // 에러 발생 시 false 반환
//                });
        return null;
    }
}
