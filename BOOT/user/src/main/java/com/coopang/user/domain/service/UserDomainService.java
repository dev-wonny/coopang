package com.coopang.user.domain.service;

import com.coopang.user.application.request.UserDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.infrastructure.repository.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserDomainService handles domain-specific business logic related to users,
 * including user creation, password changes, and password validation.
 */
@Transactional
@Service
public class UserDomainService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDomainService(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with encrypted password and stores it in the database.
     *
     * @param userDto the DTO containing user details for registration.
     * @return the newly created {@link UserEntity}.
     */
    public UserEntity createUser(UserDto userDto) {
        // 비밀번호 암호화
        final String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // 회원 등록
        UserEntity newUser = UserEntity.create(
            userDto.getUserId(),
            userDto.getEmail(),
            encodedPassword,
            userDto.getUserName(),
            userDto.getPhoneNumber(),
            userDto.getRole(),
            userDto.getSlackId(),
            userDto.getZipCode(),
            userDto.getAddress1(),
            userDto.getAddress2(),
            userDto.getNearHubId()
        );

        return userJpaRepository.save(newUser);
    }

    /**
     * Changes the user's password by encoding the new password and updating the user entity.
     *
     * @param user        the {@link UserEntity} whose password will be changed.
     * @param newPassword the new password in plain text to be encrypted.
     */
    public void changePassword(UserEntity user, String newPassword) {
        final String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);
    }

    /**
     * Validates if the provided current password matches the user's actual encoded password.
     * If the password does not match, an {@link IllegalArgumentException} is thrown with a custom error message.
     *
     * @param currentPassword the raw password provided by the user.
     * @param encodedPassword the saved encoded password stored in the database.
     * @throws IllegalArgumentException if the provided password does not match the user's actual password.
     */
    public void checkPassword(String currentPassword, String encodedPassword) {
        if (!passwordEncoder.matches(currentPassword, encodedPassword)) {
            throw new IllegalArgumentException("The provided password does not match the stored password.");
        }
    }
}
