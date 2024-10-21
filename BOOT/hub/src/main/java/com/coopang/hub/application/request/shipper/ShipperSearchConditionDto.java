package com.coopang.hub.application.request.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class ShipperSearchConditionDto {
    private UUID shipperId;
    private UUID hubId;
    private ShipperTypeEnum shipperType;

    private String hubName;// StartsWith search functionality
    private boolean isDeleted;

    @Builder
    private ShipperSearchConditionDto(
        UUID shipperId
        , UUID hubId
        , ShipperTypeEnum shipperType
        , String hubName
        , boolean isDeleted
    ) {
        this.shipperId = shipperId;
        this.hubId = hubId;
        this.shipperType = shipperType;
        this.hubName = hubName;
        this.isDeleted = isDeleted;
    }

    public static ShipperSearchConditionDto empty() {
        return ShipperSearchConditionDto.builder()
            .shipperId(null)
            .hubId(null)
            .shipperType(null)
            .hubName(null)
            .isDeleted(false)
            .build();
    }

    public static ShipperSearchConditionDto from(
        UUID shipperId
        , UUID hubId
        , String shipperType
        , String hubName
        , boolean isDeleted
    ) {
        return ShipperSearchConditionDto.builder()
            .shipperId(shipperId)
            .hubId(hubId)
            .shipperType(!ObjectUtils.isEmpty(shipperType) ? ShipperTypeEnum.getShipperTypeEnum(shipperType) : null)
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
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShipperSearchConditionDto that = (ShipperSearchConditionDto) o;
        return isDeleted == that.isDeleted
            && Objects.equals(shipperId, that.shipperId)
            && Objects.equals(hubId, that.hubId)
            && shipperType == that.shipperType
            && Objects.equals(hubName, that.hubName)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipperId, hubId, shipperType, hubName, isDeleted);
    }

    @Override
    public String toString() {
        return "ShipperSearchConditionDto{" +
            "shipperId=" + shipperId +
            ", hubId=" + hubId +
            ", shipperType=" + shipperType +
            ", hubName='" + hubName + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}