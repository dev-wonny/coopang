package com.coopang.user.application.service;

import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.user.application.response.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "RedisService")
@Service
public class RedisService {

    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String REDIS_USER_ROLE_KEY = "user:role:";
    private final String REDIS_BLACKLIST_KEY = "blacklist:";

    public RedisService(UserService usersService, RedisTemplate<String, Object> redisTemplate) {
        this.userService = usersService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 유저의 권한 정보를 Redis에서 가져옵니다. 만약 Redis에 없으면, DB에서 가져와 Redis에 캐싱하고, 캐싱된 데이터를 사용합니다.
     *
     * @param userId
     * @return
     */
    public UserRoleEnum getUserRole(String userId) {
        final String redisKey = REDIS_USER_ROLE_KEY + userId;

        // Redis에서 권한 정보 가져오기
        String roleString = (String) redisTemplate.opsForValue().get(redisKey);

        // Redis에 권한 정보가 없거나 빈 문자열인 경우
        if (!StringUtils.hasText(roleString)) {
            log.info("Role not found in Redis for user_id: {}. Fetching from DB.", userId);

            // DB 조회
            UserResponseDto user = userService.getUserInfoById(UUID.fromString(userId));

            // Redis에 캐싱
            redisTemplate.opsForValue().set(redisKey, user.getRole().getAuthority(), 1, TimeUnit.HOURS);
        }

        // Redis에서 가져온 문자열을 UserRoleEnum으로 변환
        try {
            return UserRoleEnum.getRoleEnum(roleString);
        } catch (IllegalArgumentException e) {
            // Redis에 저장된 데이터가 유효한 UserRoleEnum이 아닌 경우 예외 처리
            redisTemplate.delete(redisKey);  // 잘못된 캐시 삭제
            throw new IllegalStateException("Invalid role found in Redis for userId: " + userId, e);
        }
    }

    /**
     * 권한 수정 (updateUserRole): 권한 정보가 변경되었을 때 Redis에서 해당 유저의 권한 캐시를 삭제합니다. 이렇게 하면 다음 인증 시 DB에서 최신 권한 정보를 다시 로드하여 캐싱하게 됩니다.
     *
     * @param userId
     * @param newRole
     */
    public void updateUserRole(String userId, UserRoleEnum newRole) {
        final String redisKey = REDIS_USER_ROLE_KEY + userId;

        // Redis에 새로운 역할 정보로 덮어쓰기
        redisTemplate.opsForValue().set(redisKey, newRole.name(), 1, TimeUnit.HOURS);
        log.info("Updated user role in Redis for user_id: {} to new role: {}", userId, newRole.name());
    }

    /**
     * 사용자 역할 캐시 삭제
     *
     * @param userId
     */
    public void deleteUserRole(String userId) {
        final String redisKey = REDIS_USER_ROLE_KEY + userId;
        redisTemplate.delete(redisKey);
        log.info("Deleted user role cache in Redis for user_id: {}", userId);
    }

    /**
     * JWT 토큰을 블랙리스트에 추가합니다.
     *
     * @param token
     */
    public void addBlackToken(String token) {
        final String redisKey = REDIS_BLACKLIST_KEY + token;

        // Redis에 토큰을 블랙리스트로 저장 (예: 토큰 만료 시간만큼)
        redisTemplate.opsForValue().set(redisKey, "blacklisted", 1, TimeUnit.HOURS);
        log.info("Token added to blacklist in Redis: {}", token);
    }
}
