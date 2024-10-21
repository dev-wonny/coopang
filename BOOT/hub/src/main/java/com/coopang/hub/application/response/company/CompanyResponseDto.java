package com.coopang.hub.application.response.company;

import com.coopang.apidata.application.address.Address;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyResponseDto {
    private UUID companyId;
    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;

    private Address address;

    private boolean isDeleted;

    @Builder
    private CompanyResponseDto(
        UUID companyId
        , UUID hubId
        , UUID companyManagerId
        , String companyName
        , Address address
        , boolean isDeleted
    ) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.address = address;
        this.isDeleted = isDeleted;
    }

    public static CompanyResponseDto fromCompany(CompanyEntity company) {
        return CompanyResponseDto
            .builder()
            .companyId(company.getCompanyId())
            .hubId(company.getHubId())
            .companyManagerId(company.getCompanyManagerId())
            .companyName(company.getCompanyName())
            .address(new Address(
                company.getAddressEntity().getZipCode()
                , company.getAddressEntity().getAddress1()
                , company.getAddressEntity().getAddress2()
            ))
            .isDeleted(company.isDeleted())
            .build();
    }
}
