package com.coopang.hub.application.request.company;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CompanySearchCondition {
    private UUID companyId;
    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;//starsWith

    private String hubName;//starsWith
    private Boolean isDeleted;

    @Builder
    private CompanySearchCondition(UUID companyId, UUID hubId, UUID companyManagerId, String companyName, String hubName, Boolean isDeleted) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.hubName = hubName;
        this.isDeleted = isDeleted;
    }

    public static CompanySearchCondition from(UUID companyId, UUID hubId, UUID companyManagerId, String companyName, String hubName, Boolean isDeleted) {
        return CompanySearchCondition.builder()
                .companyId(companyId)
                .hubId(hubId)
                .companyManagerId(companyManagerId)
                .companyName(companyName)
                .hubName(hubName)
                .isDeleted(isDeleted != null ? isDeleted : false)
                .build();
    }
}
