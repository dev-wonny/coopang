package com.coopang.hub.domain.entity.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_shippers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class ShipperEntity extends BaseEntity {
    @Id
    @Column(name = "shipper_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID shipperId;

    @Column(name = "hub_id", columnDefinition = "UUID", nullable = false)
    private UUID hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipper_type", nullable = false)
    private ShipperTypeEnum shipperType;

    @Builder
    private ShipperEntity(
            UUID shipperId,
            UUID hubId,
            ShipperTypeEnum shipperType

    ) {
        this.shipperId = shipperId;
        this.hubId = hubId;
        this.shipperType = shipperType;
    }

    public static ShipperEntity create(UUID shipperId, UUID hubId, ShipperTypeEnum shipperType) {
        return ShipperEntity.builder()
                .shipperId(shipperId)
                .hubId(hubId)
                .shipperType(shipperType)
                .build();
    }

    public void updateShipperInfo(UUID hubId, ShipperTypeEnum shipperType) {
        this.hubId = hubId;
        this.shipperType = shipperType;
    }

    public void updateHubId(UUID hubId) {
        this.hubId = hubId;
    }

    public void updateShipperType(ShipperTypeEnum shipperType) {
        this.shipperType = shipperType;
    }
}
