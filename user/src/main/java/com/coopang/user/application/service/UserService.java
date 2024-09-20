package com.coopang.user.application.service;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.application.error.UserNotFoundException;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.domain.repository.UserRepository;
import com.coopang.user.domain.service.UserDomainService;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import com.coopang.user.presentation.request.SearchRequestDto;
import com.coopang.user.presentation.request.SignupRequestDto;
import com.coopang.user.presentation.request.UpdateRequestDto;
import com.coopang.user.presentation.request.UserSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "UserService")
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public UserService(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    public UserResponseDto join(SignupRequestDto request) {
        // email 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return UserResponseDto.fromUser(userDomainService.createUser(request, UserRoleEnum.valueOf(request.getRole())));
    }

    public UserResponseDto loginByEmail(String email, String password) {
        UserEntity user = getUserInfoByEmail(email);
        checkPassword(user, password);
        return UserResponseDto.fromUser(user);
    }

    // 조회
    @Cacheable(value = "users", key = "#userId")
    public UserResponseDto getUserInfoById(UUID userId) {
        UserEntity user = findById(userId);
        return UserResponseDto.fromUser(user);
    }

    public UserEntity getUserInfoByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Cacheable(value = "allUsers", key = "#condition")
    public Page<UserResponseDto> searchUsers(UserSearchCondition condition, SearchRequestDto searchRequestDto) {
        PageRequest pageRequest = PageRequest.of(
                searchRequestDto.getValidatedPage(),
                searchRequestDto.getValidatedSize(),
                Sort.by(searchRequestDto.getValidatedSortBy())
        );

        Page<UserEntity> users = userRepository.search(condition, pageRequest);
        return users.map(UserResponseDto::fromUser);
    }

    // 수정
    @CacheEvict(value = "users", key = "#userId")
    public void updateUser(UUID userId, UpdateRequestDto request) {
        UserEntity user = findById(userId);
        userDomainService.updateUser(user, request);
        log.debug("updateUser userId:{}", userId);
    }

    public void changePassword(ChangePasswordRequestDto request) {
        UUID userId = request.getUserId();
        UserEntity user = findById(userId);

        // 비밀번호 확인
        checkPassword(user, request.getCurrentPassword());
        userDomainService.changePassword(user, request);
        log.debug("changePassword userId:{}", userId);
    }

    public void blockUser(UUID userId) {
        UserEntity user = findById(userId);
        userDomainService.blockUser(user);
        log.debug("blockUser userId:{}", userId);
    }

    // 삭제
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(UUID userId) {
        UserEntity user = findById(userId);
        userDomainService.deleteUser(user);
        log.debug("deleteUser userId:{}", userId);

    }

    @Cacheable(value = "allUsers", key = "#searchRequestDto")
    public Page<UserResponseDto> getAllUsers(SearchRequestDto searchRequestDto) {
        UserSearchCondition condition = new UserSearchCondition();
        condition.setUserName(searchRequestDto.getKeyword());

        Pageable pageable = PageRequest.of(searchRequestDto.getValidatedPage(), searchRequestDto.getValidatedSize(), Sort.by(searchRequestDto.getValidatedSortBy()));
        Page<UserEntity> users = userRepository.search(condition, pageable);

        return users.map(UserResponseDto::fromUser);
    }

    @Cacheable(value = "users", key = "#userId")
    public UserEntity findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public void checkPassword(UserEntity user, String currentPassword) {
        try {
            userDomainService.checkPassword(user, currentPassword);
        } catch (Exception e) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.", e);
        }
    }
}
