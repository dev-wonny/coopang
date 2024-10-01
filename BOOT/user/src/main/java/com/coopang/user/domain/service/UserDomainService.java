package com.coopang.user.domain.service;

import com.coopang.user.application.request.UserDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.infrastructure.repository.UserJpaRepository;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserDomainService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDomainService(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 생성
    public UserEntity createUser(UserDto userDto) {
        // 비밀번호 암호화
        final String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // 회원 등록
        UserEntity newUser = UserEntity.create(userDto.getEmail(), encodedPassword, userDto.getUsername(), userDto.getPhoneNumber(), userDto.getRole(), userDto.getSlackId(),
                userDto.getZipCode(), userDto.getAddress1(), userDto.getAddress2());
        return userJpaRepository.save(newUser);
    }

    public void changePassword(UserEntity user, ChangePasswordRequestDto dto) {
        final String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.changePassword(encodedPassword);
        userJpaRepository.save(user);
    }

    public void checkPassword(UserEntity user, String currentPassword) {
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException();
        }
    }
}
