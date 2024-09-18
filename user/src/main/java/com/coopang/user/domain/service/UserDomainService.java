package com.coopang.user.domain.service;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.infrastructure.repository.UserJpaRepository;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import com.coopang.user.presentation.request.SignupRequestDto;
import com.coopang.user.presentation.request.UpdateRequestDto;
import com.coopang.user.presentation.request.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class UserDomainService {
    /*
    이 계층은 핵심 도메인 로직을 포함하고 있으며, 영속성 계층과 직접적으로 상호작용할 수 있습니다.
    인프라스트럭처 계층의 UserJpaRepository 또는
    커스텀 구현인 UserRepositoryCustomImpl을 사용하는 것이 적합합니다.
    이 리포지토리는 QueryDSL을 활용하여 복잡한 쿼리를 처리합니다.
     */

//    private final UserRepositoryCustomImpl userRepositoryCustom;

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDomainService(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원 생성 (가입)
     */
    public UserEntity createUser(SignupRequestDto dto, UserRoleEnum role) {

        // email 중복확인
        if (userJpaRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 회원 등록
        UserEntity newUser = UserEntity.create(dto, encodedPassword, role);
        return userJpaRepository.save(newUser);
    }

    // 회원 한명 정보 조회
    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserInfo(UUID userId) {
        return userJpaRepository.findById(userId);
    }

    /**
     * 사용자 정보 수정
     */
    public UserEntity updateUser(UUID userId, UpdateRequestDto dto) {
        Optional<UserEntity> optionalUser = userJpaRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.updateUserInfo(dto);
            return userJpaRepository.save(user);
        }
        throw new IllegalArgumentException("User not found");
    }

    /**
     * 비밀번호 변경
     */
    public void changeUserPassword(UUID userId, String newPassword) {
        Optional<UserEntity> optionalUser = userJpaRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            // 비밀번호 암호화
            user.changePassword(passwordEncoder.encode(newPassword));
            userJpaRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Transactional
    public void changePasswordWithCheckPastPassword(ChangePasswordRequestDto requestDto, UUID loginUserId) {
        Optional<UserEntity> userOptional = userJpaRepository.findById(requestDto.getUserId());

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("error");
        }

        UserEntity user = userOptional.get();

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 틀렸습니다.");
        }

        // 새 비밀번호 암호화
        final String newPasswordEncoded = passwordEncoder.encode(requestDto.getNewPassword());

        // 비밀번호 변경
        user.changePassword(newPasswordEncoded);
    }

    // 회원 삭제
    @Transactional
    public void deleteUser(UUID deleteUserId) {
        userJpaRepository.findUserByIdOrThrowException(deleteUserId);
    }

    // QueryDSL을 활용한 검색 기능
    public List<UserEntity> searchUsers(UserSearchCondition condition) {
        return userJpaRepository.search(condition);
    }

    // 회원 목록 조회 (페이지, 정렬 옵션 포함)
    // 정렬기능은 기본적으로 생성일순, 수정일순을 기준
    @Transactional(readOnly = true)
    public Page<UserEntity> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));

        return userJpaRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findRoleByUserId(UUID userId) {
        return userJpaRepository.findUserEntityByUserId(userId);
    }
}
