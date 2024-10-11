package com.coopang.user.application.service;

import com.coopang.apiconfig.error.UserNotFoundException;
import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.user.application.request.AddressDto;
import com.coopang.user.application.request.ChangePasswordDto;
import com.coopang.user.application.request.MyInfoUpdateDto;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.application.request.UserSearchCondition;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.domain.repository.UserRepository;
import com.coopang.user.domain.service.UserDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * UserService
 * <p>
 * This service class provides user management functionalities such as
 * registration, login, retrieving user information, and updating user details.
 * It interacts with the UserRepository for data access and UserDomainService for
 * domain logic.
 * <p>
 * Responsibilities include:
 * - Handling user-related operations like creating users, updating user info, password changes, etc.
 * - Retrieving user information based on user ID or email.
 * - Validating user passwords, roles, and ensuring valid user accounts.
 */
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

    /**
     * Register a new user.
     *
     * @param userDto the user data to create a new user.
     * @return the created user information.
     * @throws IllegalArgumentException if the email already exists.
     */
    public UserResponseDto join(UserDto userDto) {
        // email 중복 확인
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        // 서비스 레이어에서 UUID 생성
        final UUID userId = userDto.getUserId() != null ? userDto.getUserId() : UUID.randomUUID();
        userDto.setUserId(userId);

        log.debug("try join email:{}", userDto.getEmail());
        return UserResponseDto.fromUser(userDomainService.createUser(userDto));
    }

    /**
     * Log in a user by email.
     *
     * @param email    the user's email.
     * @param password the user's password.
     * @return the user information after successful login.
     */
    public UserResponseDto loginByEmail(String email, String password) {
        UserEntity user = getUserInfoByEmail(email);
        checkPassword(password, user.getPassword());
        return UserResponseDto.fromUser(user);
    }

    /**
     * Retrieve user information including blocked or deleted users.
     *
     * @param userId the user's UUID.
     * @return the user entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    /**
     * Retrieve user information excluding blocked or deleted users.
     *
     * @param userId the user's UUID.
     * @return the valid user entity.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserEntity findValidUserById(UUID userId) {
        return userRepository.findByUserIdAndIsDeletedFalseAndIsBlockFalse(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    /**
     * Retrieve current user information by user ID.
     *
     * @param userId the user's UUID.
     * @return the user information.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserResponseDto getMyInfo(UUID userId) {
        UserEntity user = findValidUserById(userId);
        return UserResponseDto.fromUser(user);
    }

    /**
     * Retrieve user information by user ID. (Admin access).
     *
     * @param userId the user's UUID.
     * @return the user information.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserResponseDto getUserInfoById(UUID userId) {
        UserEntity user = findUserById(userId);
        return UserResponseDto.fromUser(user);
    }

    /**
     * Retrieve user information by email.
     *
     * @param email the user's email.
     * @return the user entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Transactional(readOnly = true)
    public UserEntity getUserInfoByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // is_deleted 체크
        if (user.isDeleted()) {
            throw new IllegalArgumentException("This user account has been deleted.");
        }

        // is_block 체크
        if (user.isBlock()) {
            throw new IllegalArgumentException("This user account has been blocked.");
        }

        return user;
    }

    /**
     * Retrieve all users with pagination.(Admin access).
     *
     * @param pageable pagination and sorting information.
     * @return a page of users.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "allUsers", key = "#pageable")
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        return users.map(UserResponseDto::fromUser);
    }

    /**
     * Search for users based on search criteria.(Admin access).
     *
     * @param condition the search condition.
     * @param pageable  pagination and sorting information.
     * @return a page of searched users.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "allUsers", key = "#condition")
    public Page<UserResponseDto> searchUsers(UserSearchCondition condition, Pageable pageable) {
        Page<UserEntity> users = userRepository.search(condition, pageable);
        return users.map(UserResponseDto::fromUser);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userList", key = "#condition")
    public List<UserResponseDto> getUserList(UserSearchCondition condition) {
        List<UserEntity> userList = userRepository.findUserList(condition);
        return userList.stream()
                .map(UserResponseDto::fromUser)
                .toList();
    }

    /**
     * Update user information for the current user.
     *
     * @param userId the user's UUID.
     * @param dto    the data to update.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updateMyInfo(UUID userId, MyInfoUpdateDto dto) {
        UserEntity user = findValidUserById(userId);
        user.updateMyInfo(dto.getUserName(), dto.getPhoneNumber(), dto.getSlackId());
        log.debug("updateMyInfo userId:{}", userId);
    }

    /**
     * Update another user's information (Admin access).
     *
     * @param userId the user's UUID.
     * @param dto    the data to update.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updateUserInfo(UUID userId, UserDto dto) {
        UserEntity user = findUserById(userId);
        user.updateUserInfo(dto.getUserName(), dto.getPhoneNumber(), UserRoleEnum.getRoleEnum(dto.getRole()), dto.getSlackId());
        log.debug("updateUserInfo userId:{}", userId);
    }

    /**
     * Force password reset for a user (Admin or Sever API requested reset).
     *
     * @param userId      the user's UUID.
     * @param newPassword the new password.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void resetPassword(UUID userId, String newPassword) {
        UserEntity user = findValidUserById(userId);
        userDomainService.changePassword(user, newPassword);
        log.debug("resetPassword userId:{}", userId);
    }

    /**
     * Update password after validating the current password.
     *
     * @param userId the user's UUID.
     * @param dto    contains the current and new password.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updatePasswordAfterValidation(UUID userId, ChangePasswordDto dto) {
        UserEntity user = findValidUserById(userId);
        checkPassword(dto.getCurrentPassword(), user.getPassword());
        userDomainService.changePassword(user, dto.getNewPassword());
        log.debug("updatePasswordAfterValidation userId:{}", userId);
    }

    /**
     * Update the user's role (Admin access).
     *
     * @param userId the user's UUID.
     * @param role   the new role.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updateUserRole(UUID userId, String role) {
        UserEntity user = findUserById(userId);
        user.updateUserRole(UserRoleEnum.getRoleEnum(role));
        log.debug("updateUserRole userId:{}", userId);
    }

    /**
     * Update the user's Slack ID.
     *
     * @param userId  the user's UUID.
     * @param slackId the new Slack ID.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updateSlackId(UUID userId, String slackId) {
        UserEntity user = findUserById(userId);
        user.updateSlackId(slackId);
        log.debug("updateSlackId userId:{}", userId);
    }

    /**
     * Update the user's address.
     *
     * @param userId     the user's UUID.
     * @param addressDto the new address data.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updateAddress(UUID userId, AddressDto addressDto) {
        UserEntity user = findUserById(userId);
        user.updateAddress(addressDto.getZipCode(), addressDto.getAddress1(), addressDto.getAddress2());
        log.debug("updateAddress userId:{}", userId);
    }

    /**
     * Update the user's nearby hub information.
     *
     * @param userId    the user's UUID.
     * @param nearHubId the new nearby hub ID.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void updateNearHub(UUID userId, UUID nearHubId) {
        UserEntity user = findUserById(userId);
        user.updateNearHubId(nearHubId);
        log.debug("updateNearHub userId:{}", userId);
    }

    /**
     * Block the user (Admin access).
     *
     * @param userId the user's UUID.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void blockUser(UUID userId) {
        UserEntity user = findUserById(userId);
        user.blockUser();
        log.debug("blockUser userId:{}", userId);
    }

    /**
     * Unblock the user (Admin access).
     *
     * @param userId the user's UUID.
     */
    @CacheEvict(value = "users", key = "#userId")
    public void unblockUser(UUID userId) {
        UserEntity user = findUserById(userId);
        user.unblockUser();
        log.debug("unblockUser userId:{}", userId);
    }

    /**
     * Logically delete the user.
     *
     * @param userId the user's UUID. (Admin access).
     */
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(UUID userId) {
        UserEntity user = findUserById(userId);
        user.setDeleted(true);
        log.debug("deleteUser userId:{}", userId);

    }

    /**
     * Verifies the provided current password against the saved encoded password.
     * Throws an {@link IllegalArgumentException} with a detailed message if the passwords don't match.
     *
     * @param currentPassword      the raw password provided by the user.
     * @param savedEncodedPassword the encoded password stored in the database.
     * @throws IllegalArgumentException if the provided password does not match the stored encoded password.
     */
    public void checkPassword(String currentPassword, String savedEncodedPassword) {
        try {
            userDomainService.checkPassword(currentPassword, savedEncodedPassword);
        } catch (IllegalArgumentException e) {
            log.error("Password verification failed: incorrect current password provided.", e);
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.", e);
        }
    }
}
