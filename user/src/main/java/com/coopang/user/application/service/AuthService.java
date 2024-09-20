package com.coopang.user.application.service;

import com.coopang.user.application.response.LoginResponseDto;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.infrastructure.jwt.JwtUtil;
import com.coopang.user.presentation.request.LoginRequestDto;
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
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, RedisService redisService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
    }

    @Cacheable(value = "users", key = "#requestDto.email")
    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse res) {

        final UserResponseDto userResponseDto = userService.loginByEmail(requestDto.getEmail(), requestDto.getPassword());

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        final String token = jwtUtil.createToken(userResponseDto.getUserId().toString(), userResponseDto.getEmail());
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
