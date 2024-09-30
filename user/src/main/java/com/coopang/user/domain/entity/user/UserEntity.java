package com.coopang.user.domain.entity.user;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.apidata.jpa.entity.address.AddressEntity;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoleEnum role;

    @Column(name = "slack_id", length = 50)
    private String slackId;

    @Embedded
    private AddressEntity addressEntity;

    private UUID near_hub_id;

    @Column(name = "is_block", nullable = false)
    private boolean isBlock = false;


    @Builder
    public UserEntity(String email, String password, String username, String phoneNumber, UserRoleEnum role, String slackId, AddressEntity addressEntity, UUID near_hub_id, boolean isBlock) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.slackId = slackId;
        this.addressEntity = addressEntity;
        this.near_hub_id = near_hub_id;
        this.isBlock = isBlock;
    }


    public static UserEntity create(String email, String passwordEncode, String username, String phoneNumber, String role, String slackId, String zipCode, String address1, String address2) {
        return UserEntity.builder()
                .email(email)
                .password(passwordEncode)
                .username(username)
                .phoneNumber(phoneNumber)
                .role(UserRoleEnum.valueOf(role))
                .slackId(slackId)
                .addressEntity(AddressEntity.create(zipCode, address1, address2))
                .build();
    }

    public void updateUserInfo(String username, String phoneNumber, String slackId, String role) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.slackId = slackId;
        this.role = UserRoleEnum.valueOf(role);
    }

    public void changePassword(String password) {
        this.password = password;
    }


    public void setBlocked() {
        this.isBlock = true;
    }

    public void updateAddress(String zipCode, String address1, String address2) {
        this.addressEntity.updateAddress(zipCode, address1, address2);
    }
}
