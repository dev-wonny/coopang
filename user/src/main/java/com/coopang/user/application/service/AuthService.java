package com.coopang.user.application.service;

import com.coopang.user.application.response.LoginResponseDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.domain.repository.UserRepository;
import com.coopang.user.infrastructure.jwt.JwtUtil;
import com.coopang.user.presentation.request.LoginRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j(topic = "AuthService")
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(@Qualifier("userRepositoryImpl") UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse res) {
        final String email = requestDto.getEmail();
        final String password = requestDto.getPassword();

        // todo 분리
        // 사용자 확인
        final UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        final String token = jwtUtil.createToken(user.getUserId().toString(), user.getRole());

        //쿠키 저장
        jwtUtil.addJwtToCookie(token, res);
        return LoginResponseDto.builder()
                .token(token)
                .userId(user.getUserId())
                .role(user.getRole())
                .build();
    }
}
