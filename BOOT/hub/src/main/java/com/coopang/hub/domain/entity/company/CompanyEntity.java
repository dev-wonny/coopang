package com.coopang.hub.domain.entity.company;

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
@Table(name = "p_companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class CompanyEntity extends BaseEntity {

    @Id
    @Column(name = "company_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID companyId;

    @Column(name = "hub_id", columnDefinition = "UUID", nullable = false)
    private UUID hubId;

    @Column(name = "company_manager_id", columnDefinition = "UUID", nullable = false)
    private UUID companyManagerId;

    @Column(name = "company_name", length = 100, nullable = false)
    private String companyName;

    @Embedded
    private AddressEntity addressEntity;

    @Builder
    private CompanyEntity(
            UUID companyId,
            UUID hubId,
            UUID companyManagerId,
            String companyName,
            AddressEntity addressEntity
    ) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.addressEntity = addressEntity;
    }

    public static CompanyEntity create(
            UUID companyId,
            UUID hubId,
            UUID companyManagerId,
            String companyName,
            String zipCode,
            String address1,
            String address2
    ) {
        return CompanyEntity.builder()
                .companyId(companyId)
                .hubId(hubId)
                .companyManagerId(companyManagerId)
                .companyName(companyName)
                .addressEntity(AddressEntity.create(zipCode, address1, address2))
                .build();
    }

    public void updateCompanyInfo(String companyName, UUID hubId, UUID companyManagerId, String zipCode, String address1, String address2) {
        this.companyName = companyName;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.addressEntity.updateAddress(zipCode, address1, address2);
    }

    public void updateHubId(UUID hubId) {
        this.hubId = hubId;
    }

    public void updateCompanyManagerId(UUID companyManagerId) {
        this.companyManagerId = companyManagerId;
    }

    public void updateCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void updateCompanyAddress(String zipCode, String address1, String address2) {
        this.addressEntity.updateAddress(zipCode, address1, address2);
    }
}
