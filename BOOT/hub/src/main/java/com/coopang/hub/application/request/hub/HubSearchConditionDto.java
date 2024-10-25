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
    private HubSearchConditionDto(
        UUID hubId
        , String hubName
        , UUID hubManagerId
        , boolean isDeleted
    ) {
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

    public static HubSearchConditionDto from(
        UUID hubId
        , String hubName
        , UUID hubManagerId
        , boolean isDeleted
    ) {
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
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HubSearchConditionDto that = (HubSearchConditionDto) o;
        return isDeleted == that.isDeleted
            && Objects.equals(hubId, that.hubId)
            && Objects.equals(hubName, that.hubName)
            && Objects.equals(hubManagerId, that.hubManagerId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hubId, hubName, hubManagerId, isDeleted);
    }

    @Override
    public String toString() {
        return "HubSearchConditionDto{" +
            "hubId=" + hubId +
            ", hubName='" + hubName + '\'' +
            ", hubManagerId=" + hubManagerId +
            ", isDeleted=" + isDeleted +
            '}';
    }
}