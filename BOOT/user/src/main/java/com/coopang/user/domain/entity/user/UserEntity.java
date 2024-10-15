package com.coopang.user.domain.entity.user;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.apidata.jpa.entity.address.AddressEntity;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID userId;

    @Email
    @Column(name = "user_email", length = 320, nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "user_password", length = 60, nullable = false)
    private String password;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "user_phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRoleEnum role;

    @Column(name = "slack_id", length = 50)
    private String slackId;

    @Embedded
    private AddressEntity addressEntity;

    @Column(name = "near_hub_id")
    private UUID nearHubId;

    @Column(name = "is_block", nullable = false)
    private boolean isBlock = false;


    @Builder
    private UserEntity(
            UUID userId,
            String email,
            String password,
            String userName,
            String phoneNumber,
            UserRoleEnum role,
            String slackId,
            AddressEntity addressEntity,
            UUID nearHubId,
            boolean isBlock
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.slackId = slackId;
        this.addressEntity = addressEntity;
        this.nearHubId = nearHubId;
        this.isBlock = isBlock;
    }

    public static UserEntity create(
            UUID userId,
            String email,
            String passwordEncode,
            String userName,
            String phoneNumber,
            String role,
            String slackId,
            String zipCode,
            String address1,
            String address2,
            UUID nearHubId
    ) {
        return UserEntity.builder()
                .userId(userId != null ? userId : UUID.randomUUID())
                .email(email)
                .password(passwordEncode)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .role(UserRoleEnum.getRoleEnum(role))
                .slackId(slackId)
                .addressEntity(AddressEntity.create(zipCode, address1, address2))
                .nearHubId(nearHubId)
                .build();
    }

    public void updateUserInfo(String userName,
                               String phoneNumber,
                               String role,
                               String slackId
    ) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.role = UserRoleEnum.getRoleEnum(role);
        this.slackId = slackId;
    }

    public void updateMyInfo(String userName,
                             String phoneNumber,
                             String slackId
    ) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.slackId = slackId;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateUserRole(UserRoleEnum role) {
        this.role = role;
    }

    public void updateSlackId(String slackId) {
        this.slackId = slackId;
    }

    public void updateAddress(String zipCode, String address1, String address2) {
        this.addressEntity.updateAddress(zipCode, address1, address2);
    }

    public void updateNearHubId(UUID nearHubId) {
        this.nearHubId = nearHubId;
    }

    public void blockUser() {
        this.isBlock = true;
    }

    public void unblockUser() {
        this.isBlock = false;
    }
}
