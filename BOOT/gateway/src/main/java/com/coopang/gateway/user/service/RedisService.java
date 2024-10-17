package com.coopang.gateway.user.service;

import static com.coopang.authcommon.constants.RedisConstants.REDIS_BLACKLIST_KEY;
import static com.coopang.authcommon.constants.RedisConstants.REDIS_USER_ROLE_KEY;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j(topic = "RedisService")
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthService authService;

    public RedisService(RedisTemplate<String, Object> redisTemplate, AuthService authService) {
        this.redisTemplate = redisTemplate;
        this.authService = authService;
    }

    public Mono<Boolean> isBlacklistedToken(String token) {
        return Mono.defer(() -> {
            Boolean isBlacklisted = redisTemplate.hasKey(REDIS_BLACKLIST_KEY + token);
            if (Boolean.TRUE.equals(isBlacklisted)) {
                log.warn("Token is blacklisted: {}", token);
                return Mono.just(true);
            }
            return Mono.just(false);
        });
    }

    public Mono<String> getUserRole(String userId, String role) {
        // 1. 레디스에서 role 값 가져옴, 없으면 인증 서버에서 요청해서 받는다
        final String redisKey = REDIS_USER_ROLE_KEY + userId;
        return Mono.justOrEmpty((String) redisTemplate.opsForValue().get(redisKey))
            // 값이 없을 때 비동기 작업 수행
            .switchIfEmpty(
                // 2. AuthService에서 역할을 가져오고 Redis에 저장(인증서버)
                authService.getRoleFromAuthService(userId)
            )
            // 3. 요청된 역할과 저장된 역할이 일치하는지 확인
            .filter(storedRole -> storedRole.equals(role))
            // 일치하지 않거나 오류가 발생한 경우
            // 4. 역할이 일치하면 반환
            .switchIfEmpty(Mono.error(new IllegalStateException("Role mismatch or not found")));
    }
}
