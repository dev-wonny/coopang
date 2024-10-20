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
    private ShipperSearchConditionDto(UUID shipperId, UUID hubId, ShipperTypeEnum shipperType, String hubName, boolean isDeleted) {
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

    public static ShipperSearchConditionDto from(UUID shipperId, UUID hubId, String shipperType, String hubName, boolean isDeleted) {
        return ShipperSearchConditionDto.builder()
            .shipperId(shipperId)
            .hubId(hubId)
            .shipperType(!ObjectUtils.isEmpty(shipperType) ? ShipperTypeEnum.getRoleEnum(shipperType) : null)
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
            ShipperSearchConditionDto that = (ShipperSearchConditionDto) o;
            return
                Objects.equals(this.shipperId, that.shipperId)
                    && Objects.equals(this.hubId, that.hubId)
                    && this.shipperType == that.shipperType
                    && Objects.equals(this.hubName, that.hubName)
                    && Objects.equals(this.isDeleted, that.isDeleted);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shipperId, this.hubId, this.shipperType, this.hubName, this.isDeleted);
    }

    @Override
    public String toString() {
        return "ShipperSearchConditionDto(shipperId=" + this.shipperId + ", hubId=" + this.hubId + ", shipperType=" + this.shipperType + ", hubName=" + this.hubName + ", isDeleted=" + this.isDeleted +
            ")";
    }
}