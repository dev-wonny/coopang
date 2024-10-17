package com.coopang.order.domain.entity.order;

import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.apidata.jpa.entity.address.AddressEntity;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "order_id", columnDefinition = "UUID", unique = true, nullable = false)
    private UUID orderId;

    @Column(name = "user_id", columnDefinition = "UUID", nullable = false)
    private UUID userId;

    @Column(name = "product_id", columnDefinition = "UUID", nullable = false)
    private UUID productId;

    @Embedded
    private AddressEntity addressEntity;

    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;

    @Column(name = "order_single_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal orderSinglePrice;

    @Column(name = "order_total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal orderTotalPrice;

    @Column(name = "company_id", columnDefinition = "UUID")
    private UUID companyId;

    @Column(name = "product_hub_id", columnDefinition = "UUID")
    private UUID productHubId;

    @Column(name = "near_hub_id", columnDefinition = "UUID")
    private UUID nearHubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatusEnum orderStatus;

    @Builder
    private OrderEntity(
            UUID orderId,
            UUID userId,
            UUID productId,
            AddressEntity addressEntity,
            Integer orderQuantity,
            BigDecimal orderSinglePrice,
            BigDecimal orderTotalPrice,
            UUID companyId,
            UUID productHubId,
            UUID nearHubId,
            OrderStatusEnum orderStatus
    ){
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.addressEntity = addressEntity;
        this.orderQuantity = orderQuantity;
        this.orderSinglePrice = orderSinglePrice;
        this.orderTotalPrice = orderTotalPrice;
        this.companyId = companyId;
        this.productHubId = productHubId;
        this.nearHubId = nearHubId;
        this.orderStatus = orderStatus;
    }

    public static OrderEntity create(
            UUID userId,
            UUID productId,
            String zipCode,
            String address1,
            String address2,
            Integer orderQuantity,
            BigDecimal orderSinglePrice,
            BigDecimal orderTotalPrice,
            UUID productHubId,
            UUID nearHubId
    ){
        return OrderEntity.builder()
                .userId(userId)
                .productId(productId)
                .addressEntity(AddressEntity.create(zipCode,address1,address2))
                .orderQuantity(orderQuantity)
                .orderSinglePrice(orderSinglePrice)
                .orderTotalPrice(orderTotalPrice)
                .orderStatus(OrderStatusEnum.READY)
                .productHubId(productHubId)
                .nearHubId(nearHubId)
                .build();
    }

    public void updateOrder(
            UUID nearHubId,
            String zipCode,
            String address1,
            String address2
    ){
        this.nearHubId = nearHubId;
        this.addressEntity.updateAddress(zipCode,address1,address2);
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }
}
