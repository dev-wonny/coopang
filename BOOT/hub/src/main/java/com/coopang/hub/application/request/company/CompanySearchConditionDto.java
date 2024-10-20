package com.coopang.hub.application.request.company;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class CompanySearchConditionDto {
    private UUID companyId;
    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;// StartsWith search functionality

    private String hubName;// StartsWith search functionality
    private boolean isDeleted;

    @Builder
    private CompanySearchConditionDto(UUID companyId, UUID hubId, UUID companyManagerId, String companyName, String hubName, boolean isDeleted) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.hubName = hubName;
        this.isDeleted = isDeleted;
    }

    public static CompanySearchConditionDto empty() {
        return CompanySearchConditionDto.builder()
            .companyId(null)
            .hubId(null)
            .companyManagerId(null)
            .companyName(null)
            .hubName(null)
            .isDeleted(false)
            .build();
    }

    public static CompanySearchConditionDto from(UUID companyId, UUID hubId, UUID companyManagerId, String companyName, String hubName, boolean isDeleted) {
        return CompanySearchConditionDto.builder()
            .companyId(companyId)
            .hubId(hubId)
            .companyManagerId(companyManagerId)
            .companyName(companyName)
            .hubName(hubName)
            .isDeleted(!ObjectUtils.isEmpty(isDeleted) && isDeleted)
            .build();
    }

    public void setIsDeletedFalse() {
        this.isDeleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            CompanySearchConditionDto that = (CompanySearchConditionDto) o;
            return
                Objects.equals(this.companyId, that.companyId)
                    && Objects.equals(this.hubId, that.hubId)
                    && Objects.equals(this.companyManagerId, that.companyManagerId)
                    && Objects.equals(this.companyName, that.companyName)
                    && Objects.equals(this.hubName, that.hubName)
                    && Objects.equals(this.isDeleted, that.isDeleted);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.companyId, this.hubId, this.companyManagerId, this.companyName, this.hubName, this.isDeleted);
    }

    @Override
    public String toString() {
        return "CompanySearchConditionDto(companyId=" + this.companyId + ", hubId=" + this.hubId + ", companyManagerId=" + this.companyManagerId + ", companyName=" + this.companyName + ", hubName=" +
            this.hubName + ", isDeleted=" + this.isDeleted + ")";
    }
}
