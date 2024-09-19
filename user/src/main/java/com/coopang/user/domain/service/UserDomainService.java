package com.coopang.user.domain.service;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.infrastructure.repository.UserJpaRepository;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import com.coopang.user.presentation.request.SignupRequestDto;
import com.coopang.user.presentation.request.UpdateRequestDto;
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
    public UserEntity createUser(SignupRequestDto dto, UserRoleEnum role) {
        // 비밀번호 암호화
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 회원 등록
        UserEntity newUser = UserEntity.create(dto, encodedPassword, role);
        return userJpaRepository.save(newUser);
    }

    // 수정
    public void updateUser(UserEntity user, UpdateRequestDto dto) {
        user.updateUserInfo(dto);
        userJpaRepository.save(user);
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

    public void blockUser(UserEntity user) {
        user.setBlocked();
        userJpaRepository.save(user);
    }

    // 삭제
    public void deleteUser(UserEntity user) {
        user.setDeleted(true);
        userJpaRepository.save(user);
    }
}
