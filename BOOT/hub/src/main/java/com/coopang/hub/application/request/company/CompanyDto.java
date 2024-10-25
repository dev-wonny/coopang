package com.coopang.hub.application.request.company;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDto {
    private UUID companyId;

    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;

    private String zipCode;
    private String address1;
    private String address2;

    @Builder
    private CompanyDto(
        UUID companyId
        , UUID hubId
        , UUID companyManagerId
        , String companyName
        , String zipCode
        , String address1
        , String address2
    ) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
    }

    public static CompanyDto of(
        UUID companyId
        , UUID hubId
        , UUID companyManagerId
        , String companyName
        , String zipCode
        , String address1
        , String address2
    ) {
        return CompanyDto.builder()
            .companyId(companyId)
            .hubId(hubId)
            .companyManagerId(companyManagerId)
            .companyName(companyName)
            .zipCode(zipCode)
            .address1(address1)
            .address2(address2)
            .build();
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
            "companyId=" + companyId +
            ", hubId=" + hubId +
            ", companyManagerId=" + companyManagerId +
            ", companyName='" + companyName + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            '}';
    }

    public void createId(UUID companyId) {
        this.companyId = companyId;
    }
}