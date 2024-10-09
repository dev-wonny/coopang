package com.coopang.hub.domain.entity.hub;

import com.coopang.apidata.jpa.entity.address.AddressEntity;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_hubs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class HubEntity extends BaseEntity {

    @Id
    @Column(name = "hub_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID hubId;

    @Column(name = "hub_name", length = 100, nullable = false)
    private String hubName;

    @Column(name = "hub_manager_id", columnDefinition = "UUID")
    private UUID hubManagerId;

    @Embedded
    private AddressEntity addressEntity;

    @Builder
    private HubEntity(UUID hubId, String hubName, UUID hubManagerId, AddressEntity addressEntity) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.hubManagerId = hubManagerId;
        this.addressEntity = addressEntity;
    }

    public static HubEntity create(
            UUID hubId,
            String hubName,
            UUID hubManagerId,
            String zipCode,
            String address1,
            String address2
    ) {
        return HubEntity.builder()
                .hubId(hubId)
                .hubName(hubName)
                .hubManagerId(hubManagerId)
                .addressEntity(AddressEntity.create(zipCode, address1, address2))
                .build();
    }

    public void updateHubInfo(String hubName, UUID hubManagerId, String zipCode, String address1, String address2) {
        this.hubName = hubName;
        this.hubManagerId = hubManagerId;
        this.addressEntity.updateAddress(zipCode, address1, address2);
    }
}
