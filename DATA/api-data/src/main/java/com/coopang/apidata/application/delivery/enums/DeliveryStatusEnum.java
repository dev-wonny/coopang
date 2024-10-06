package com.coopang.apidata.application.delivery.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatusEnum {
    DELIVERY_CANCELED("배송 취소 (주문 취소)"),
    PENDING("대기중 (배송기사 배정 안함)"),
    HUB_DELIVERY_ASSIGNMENT_IN_PROGRESS("허브 배송 배차 중"),
    HUB_DELIVERY_ASSIGNMENT_COMPLETED("허브 배송 배차 완료"),
    MOVING_TO_HUB("허브 이동중"),
    ARRIVED_AT_DESTINATION_HUB("목적지 허브 도착"),
    CUSTOMER_DELIVERY_ASSIGNMENT_IN_PROGRESS("고객 배송 배차중"),
    CUSTOMER_DELIVERY_ASSIGNMENT_COMPLETED("고객 배송 배차 완료"),
    MOVING_TO_CUSTOMER("고객에게 이동중"),
    DELIVERY_COMPLETED_TO_CUSTOMER("고객에게 전달 완료");

    private final String description;

    DeliveryStatusEnum(String description) {
        this.description = description;
    }

    /**
     * 주어진 상태 문자열에 해당하는 DeliveryStatusEnum을 반환합니다.
     *
     * @param status 배송 상태 문자열
     * @return 해당하는 DeliveryStatusEnum
     * @throws IllegalArgumentException 유효하지 않은 상태 문자열일 경우
     */
    public static DeliveryStatusEnum getStatusEnum(String status) {
        for (DeliveryStatusEnum deliveryStatus : values()) {
            if (status.equals(deliveryStatus.name())) {
                return deliveryStatus;
            }
        }
        throw new IllegalArgumentException("Invalid delivery status: " + status);
    }
}