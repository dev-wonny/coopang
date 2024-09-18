package com.coopang.user.application.service;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.domain.service.UserDomainService;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import com.coopang.user.presentation.request.SignupRequestDto;
import com.coopang.user.presentation.request.UpdateRequestDto;
import com.coopang.user.presentation.request.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    /*
    이 계층에서는 비즈니스 로직을 처리하고 외부 애플리케이션에서 호출되는 서비스 역할을 합니다.
    이 서비스는 데이터베이스나 인프라와 직접 상호작용하는 대신,
    도메인 계층을 통해 간접적으로 접근하는 것이 이상적입니다.
     */


    private final UserDomainService userDomainService;

    public UserService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    /**
     * 회원 생성 요청 처리
     */
    public UserResponseDto join(SignupRequestDto dto) {
        return UserResponseDto.fromUser(userDomainService.createUser(dto, UserRoleEnum.getRoleEnum(dto.getRole())));
    }

    /**
     * 회원 조회
     */
    public UserResponseDto getUserInfo(UUID userId) {
        return UserResponseDto.fromUser(userDomainService.getUserInfo(userId).get());
    }

    /**
     * 사용자 정보 수정 요청 처리
     */
    public UserResponseDto updateUserInfo(UUID userId, UpdateRequestDto dto) {
        return UserResponseDto.fromUser(userDomainService.updateUser(userId, dto));
    }


    // 회원 삭제
    public void deleteUser(UUID deleteUserId) {
        userDomainService.deleteUser(deleteUserId);
    }

    /**
     * 비밀번호 변경 요청 처리
     */
    public void changePassword(UUID userId, String newPassword) {
        userDomainService.changeUserPassword(userId, newPassword);
    }

    public void changePasswordWithCheckPastPassword(UUID loginUserId, ChangePasswordRequestDto requestDto) {
        userDomainService.changePasswordWithCheckPastPassword(requestDto, loginUserId);
    }

    public Page<UserResponseDto> searchUsers(UserSearchCondition condition) {
        List<UserEntity> userList = userDomainService.searchUsers(condition);
        return null;
    }

    public Page<UserResponseDto> getAllUsers(int page, int size, String sortBy) {
        Page<UserEntity> userPage = userDomainService.getAllUsers(page, size, sortBy);
        return userPage.map(UserResponseDto::fromUser);
    }

    public String findRoleByUserId(String userId) {
        return userDomainService.findRoleByUserId(UUID.fromString(userId))
                .map(user -> user.getRole().name())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }


}
