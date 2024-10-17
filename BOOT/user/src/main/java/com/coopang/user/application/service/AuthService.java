package com.coopang.user.application.service;

import com.coopang.user.application.response.LoginResponseDto;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.infrastructure.jwt.AuthJwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j(topic = "AuthService")
@Service
public class AuthService {

    private final UserService userService;
    private final RedisService redisService;
    private final AuthJwtUtil jwtUtil;

    public AuthService(UserService userService, RedisService redisService, AuthJwtUtil jwtUtil) {
        this.userService = userService;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
    }

    @Cacheable(value = "users", key = "#email")
    public LoginResponseDto login(String email, String password, HttpServletResponse res) {
        final UserResponseDto userResponseDto = userService.loginByEmail(email, password);

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        final String token = jwtUtil.createToken(userResponseDto.getUserId().toString(), userResponseDto.getEmail(), userResponseDto.getRole());
        log.debug("login and make token:{}, userId:{}, email:{}, role: {}", token, userResponseDto.getUserId().toString(), userResponseDto.getEmail(), userResponseDto.getRole());

        //쿠키 저장
        jwtUtil.addJwtToCookie(token, res);
        return LoginResponseDto.builder()
            .token(token)
            .userId(userResponseDto.getUserId())
            .role(userResponseDto.getRole())
            .build();
    }

    @CacheEvict(value = "users", key = "#userId")
    public void logout(UUID userId, String extractedToken) {
        redisService.addBlackToken(extractedToken);
        log.debug("logout extractedToken:{}", extractedToken);
    }
}
