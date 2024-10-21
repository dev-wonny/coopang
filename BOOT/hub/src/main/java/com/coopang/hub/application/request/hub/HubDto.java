package com.coopang.hub.application.request.hub;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class HubDto {
    private UUID hubId;
    private String hubName;
    private UUID hubManagerId;

    private String zipCode;
    private String address1;
    private String address2;

    @Builder
    private HubDto(
        UUID hubId
        , String hubName
        , UUID hubManagerId
        , String zipCode
        , String address1
        , String address2
    ) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.hubManagerId = hubManagerId;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
    }

    public static HubDto of(
        UUID hubId
        , String hubName
        , UUID hubManagerId
        , String zipCode
        , String address1
        , String address2
    ) {
        return HubDto.builder()
            .hubId(hubId)
            .hubName(hubName)
            .hubManagerId(hubManagerId)
            .zipCode(zipCode)
            .address1(address1)
            .address2(address2)
            .build();
    }

    @Override
    public String toString() {
        return "HubDto{" +
            "hubId=" + hubId +
            ", hubName='" + hubName + '\'' +
            ", hubManagerId=" + hubManagerId +
            ", zipCode='" + zipCode + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            '}';
    }

    public void createId(UUID hubId) {
        this.hubId = hubId;
    }
}