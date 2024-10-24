package com.coopang.delivery.application.request.delivery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryDto {
    private UUID orderId;
    private UUID departureHubId;
    private UUID destinationHubId;
    private String zipCode;
    private String address1;
    private String address2;
    private UUID hubShipperId;

    @Builder
    private DeliveryDto(
            UUID orderId
            , UUID departureHubId
            , UUID destinationHubId
            , String zipCode
            , String address1
            , String address2
            , UUID hubShipperId
    ){
        this.orderId = orderId;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.hubShipperId = hubShipperId;
    }

    public static DeliveryDto of(
            UUID orderId
            , UUID departureHubId
            , UUID destinationHubId
            , String zipCode
            , String address1
            , String address2
            , UUID hubShipperId
    ){
        return DeliveryDto.builder()
                .orderId(orderId)
                .departureHubId(departureHubId)
                .destinationHubId(destinationHubId)
                .zipCode(zipCode)
                .address1(address1)
                .address2(address2)
                .hubShipperId(hubShipperId)
                .build();
    }

    @Override
    public String toString() {
        return "DeliveryDto{" +
                "orderId=" + orderId +
                ", departureHubId=" + departureHubId +
                ", destinationHubId=" + destinationHubId +
                ", zipCode='" + zipCode + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", hubShipperId=" + hubShipperId +
                '}';
    }
}
