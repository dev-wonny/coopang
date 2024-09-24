package com.coopang.user.domain.entity.user;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.domain.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;


@DynamicUpdate
@Entity
@Table(name = "p_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class UserEntity extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID userId;

    @Email
    @Column(name = "user_email", length = 320, nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", length = 60, nullable = false)
    private String password;

    @Column(name = "user_name", length = 50, nullable = false)
    private String username;

    @Column(name = "user_phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "slack_id", length = 50)
    private String slackId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoleEnum role;

    @Column(name = "is_block", nullable = false)
    private boolean isBlock = false;


    @Builder
    private UserEntity(String email, String password, String username, String phoneNumber, String slackId, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.slackId = slackId;
        this.role = role;
    }

    public static UserEntity create(UserDto dto, String passwordEncode) {
        return UserEntity.builder()
                .email(dto.getEmail())
                .password(passwordEncode)
                .username(dto.getUsername())
                .phoneNumber(dto.getPhoneNumber())
                .slackId(dto.getSlackId())
                .role(UserRoleEnum.valueOf(dto.getRole()))
                .build();

    }

    public void updateUserInfo(UserDto dto) {
        this.username = dto.getUsername();
        this.phoneNumber = dto.getPhoneNumber();
        this.slackId = dto.getSlackId();
        this.role = UserRoleEnum.valueOf(dto.getRole());
    }

    public void changePassword(String password) {
        this.password = password;
    }


    public void setBlocked() {
        this.isBlock = true;
    }
}
