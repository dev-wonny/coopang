package com.coopang.user.presentation.controller;

import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ID;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * AuthController handles the authentication and authorization operations, including login, logout, and user validation based on headers.
 */
@Tag(name = "AuthController API", description = "AuthController API")
@Slf4j(topic = "AuthController")
@RestController
@RequestMapping("/auth/v1")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles the login request. Validates the email and password, generates a JWT token upon successful authentication,
     * and sends it in the response.
     * <p>
     * * jwt 토큰이 없는 상태
     * * 로그인 요청
     * * - email과 비밀번호가 일치하는지 확인
     * * - 일치하면 토큰을 발행한다
     * * - 토큰을 쿠키에 담는다, 게이트웨이 서버로 보낸다, 게이트웨이 서버는 유저에게 jwt 토큰을 준다
     *
     * @param requestDto the login request containing the user's email and password.
     * @param res        the HttpServletResponse object for adding the JWT token to the response header or cookies.
     * @return the response containing the login result, including the JWT token and user information.
     * @throws IllegalArgumentException if the email or password is incorrect.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        final LoginResponseDto responseDto = authService.login(requestDto.getEmail(), requestDto.getPassword(), res);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    /**
     * Validates the user based on the provided user ID in the header. Uses Spring Security's UserDetails to verify
     * that the authenticated user matches the user ID in the header.
     * <p>
     * * headerID로 DB 조회 검증이 필요한 경우
     * * Spring Security 활용
     * * UserDetailsImpl 활용
     * * header userId로 인증 처리
     *
     * @param userDetails  the authenticated user's details from Spring Security.
     * @param headerUserId the user ID provided in the header.
     * @return the user information if validation is successful.
     * @throws AccessDeniedException if the authenticated user's ID does not match the header's user ID.
     */
    @PostMapping("/header")
    public ResponseEntity<UserResponseDto> getUserInfoByHeader(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestHeader(value = HEADER_USER_ID, required = true) String headerUserId) {

        UserEntity user = userDetails.getUser();
        // 현재 인증된 사용자의 userId
        final UUID authenticatedUserId = user.getUserId();

        if (!headerUserId.equals(authenticatedUserId.toString())) {
            // 인증된 사용자와 헤더의 userId가 일치하지 않는 경우 예외 처리
            throw new AccessDeniedException("인증된 사용자와 헤더의 userId가 일치하지 않음");
        }

        return new ResponseEntity<>(UserResponseDto.fromUser(user), HttpStatus.OK);
    }

    /**
     * Handles the logout request. Logs out the user by adding their JWT token to a blacklist in Redis. No session
     * management is involved since JWT tokens are used.
     * <p>
     * * 로그아웃
     * * - 레디스에 블랙리스트jwt 을 넣는다
     * * - 세션 사용하지 않음
     * * - 클라이언트에서 jwt 토큰 쿠키 삭제
     *
     * @param userIdHeader the user ID extracted from the request header (HEADER_USER_ID).
     * @param tokenHeader  the JWT token extracted from the request header ("X-Token").
     * @return a response indicating that the logout was successful.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = HEADER_USER_ID, required = true) UUID userIdHeader,
            @RequestHeader(value = "X-Token", required = true) String tokenHeader) {
        authService.logout(userIdHeader, tokenHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
