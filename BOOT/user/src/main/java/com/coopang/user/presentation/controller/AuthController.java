package com.coopang.user.presentation.controller;

import com.coopang.apiconfig.error.AccessDeniedException;
import com.coopang.user.application.response.LoginResponseDto;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.application.service.AuthService;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.infrastructure.security.UserDetailsImpl;
import com.coopang.user.presentation.request.LoginRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "AuthController API", description = "AuthController API")
@Slf4j(topic = "AuthController")
@RestController
@RequestMapping("/auth/v1")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
            jwt 토큰이 없는 상태
                로그인 요청
                - email과 비밀번호가 일치하는지 확인
                - 일치하면 토큰을 발행한다
                - 토큰을 쿠키에 담는다, 게이트웨이 서버로 보낸다, 게이트웨이 서버는 유저에게 jwt 토큰을 준다
        */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        final LoginResponseDto responseDto = authService.login(requestDto.getEmail(), requestDto.getPassword(), res);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 실험용
     * Spring Security 활용
     * UserDetailsImpl 활용
     * header userId로 인증 처리
     */
    @PostMapping("/header")
    public ResponseEntity<UserResponseDto> getUserInfoByHeader(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestHeader(value = "X-User-Id", required = true) String headerUserId) {

        UserEntity user = userDetails.getUser();
        // 현재 인증된 사용자의 userId
        final UUID authenticatedUserId = user.getUserId();

        if (!headerUserId.equals(authenticatedUserId.toString())) {
            // 인증된 사용자와 헤더의 userId가 일치하지 않는 경우 예외 처리
            throw new AccessDeniedException("인증된 사용자와 헤더의 userId가 일치하지 않음");
        }

        return ResponseEntity.ok(UserResponseDto.fromUser(user));
    }

    /*
        로그아웃
        - 레디스에 블랙리스트jwt 을 넣는다
        - 세션은 안쓰니 세션 제거는 필요없다
        - 클라이언트에서 jwt 토큰 쿠키 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "X-User-Id", required = true) UUID userId,
            @RequestHeader(value = "X-Token", required = true) String extractedToken
    ) {
        authService.logout(userId, extractedToken);
        return ResponseEntity.ok().build();
    }
}
