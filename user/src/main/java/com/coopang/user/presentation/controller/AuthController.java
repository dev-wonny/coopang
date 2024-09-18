package com.coopang.user.presentation.controller;

import com.coopang.user.application.response.LoginResponseDto;
import com.coopang.user.application.service.AuthService;
import com.coopang.user.application.service.RedisService;
import com.coopang.user.presentation.request.LoginRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController API", description = "AuthController API")
@Slf4j
@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final RedisService redisService;
    private final AuthService authService;

    public AuthController(RedisService redisService, AuthService authService) {
        this.redisService = redisService;
        this.authService = authService;
    }

    /*
                jwt 토큰이 없는 상태
                    1. 로그인 요청
                    - email과 비밀번호가 일치하는지 확인
                    - 일치하면 토큰을 발행한다
                    - 토큰을 쿠키에 담는다, 게이트웨이 서버로 보낸다, 게이트웨이 서버는 유저에게 jwt 토큰을 준다
                */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        final LoginResponseDto responseDto = authService.login(requestDto, res);
        // redis role 추가
        redisService.updateUserRole(responseDto.getUserId().toString(), responseDto.getRole());
        return ResponseEntity.ok(responseDto);
    }

    /*
    5. 로그아웃
        - 레디스에 블랙리스트jwt 을 넣는다
        - 세션은 안쓰니 세션 제거는 필요없다
        - 클라이언트에서 jwt 토큰 쿠키 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String tokenValue
            , @RequestHeader(value = "X-Token", required = true) String extractedToken
            , @RequestHeader(value = "X-User-Id", required = true) String userId
            , @RequestHeader(value = "X-Role", required = true) String role
            , HttpServletResponse res

    ) {
        // 토큰을 블랙리스트에 추가
        redisService.addBlackToken(extractedToken);
        return ResponseEntity.ok().build();
    }
}
