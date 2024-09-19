package com.coopang.gateway.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j(topic = "RedisService")
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthService authService;
    private final String REDIS_USER_ROLE_KEY = "user:role:";
    private final String REDIS_BLACKLIST_KEY = "blacklist:";

    public RedisService(RedisTemplate<String, Object> redisTemplate, AuthService authService) {
        this.redisTemplate = redisTemplate;
        this.authService = authService;
    }

    public boolean isBlacklistedToken(String token) {
        boolean isBlacklisted = redisTemplate.hasKey(REDIS_BLACKLIST_KEY + token);
        if (isBlacklisted) {
            log.warn("Token is blacklisted: {}", token);
        }
        return isBlacklisted;
    }

    public Mono<String> getRoleFromRedisMono(String userId, String role) {
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


    public Optional<String> getRoleFromRedis(String userId, String role) {
        String redisKey = REDIS_USER_ROLE_KEY + userId;
        String storedRole = (String) redisTemplate.opsForValue().get(redisKey);

        // 1. Redis에 저장된 역할이 없거나, 공백 문자열인 경우
        if (storedRole == null || storedRole.isBlank()) {
            log.info("Role for user {} not found in Redis, fetching from AuthService", userId);

            // 2. AuthService에서 역할을 가져옴
            storedRole = String.valueOf(authService.getRoleFromAuthService(userId));
        }

        // 3. 요청된 역할과 저장된 역할이 일치하는지 확인
        if (!role.equals(storedRole)) {
            log.warn("Role mismatch for user {}: expected {}, but found {}", userId, role, storedRole);
            return Optional.empty();
        }

        // 4. 역할이 일치하면 Optional로 반환
        return Optional.of(storedRole);
    }
}
