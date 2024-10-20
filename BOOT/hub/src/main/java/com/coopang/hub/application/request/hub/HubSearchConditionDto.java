package com.coopang.hub.application.request.hub;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class HubSearchConditionDto {
    private UUID hubId;
    private String hubName;// StartsWith search functionality
    private UUID hubManagerId;
    private boolean isDeleted;

    @Builder
    private HubSearchConditionDto(UUID hubId, String hubName, UUID hubManagerId, boolean isDeleted) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.hubManagerId = hubManagerId;
        this.isDeleted = isDeleted;
    }

    public static HubSearchConditionDto empty() {
        return HubSearchConditionDto.builder()
            .hubId(null)
            .hubName(null)
            .hubManagerId(null)
            .isDeleted(false)
            .build();
    }

    public static HubSearchConditionDto from(UUID hubId, String hubName, UUID hubManagerId, boolean isDeleted) {
        return HubSearchConditionDto.builder()
            .hubId(hubId)
            .hubName(hubName)
            .hubManagerId(hubManagerId)
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
            HubSearchConditionDto that = (HubSearchConditionDto) o;
            return
                Objects.equals(this.hubId, that.hubId)
                    && Objects.equals(this.hubName, that.hubName)
                    && Objects.equals(this.hubManagerId, that.hubManagerId)
                    && Objects.equals(this.isDeleted, that.isDeleted);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hubId, this.hubName, this.hubManagerId, this.isDeleted);
    }

    @Override
    public String toString() {
        return "HubSearchConditionDto(hubId=" + this.hubId + ", hubName=" + this.hubName + ", hubManagerId=" + this.hubManagerId + ", isDeleted=" + this.isDeleted + ")";
    }
}