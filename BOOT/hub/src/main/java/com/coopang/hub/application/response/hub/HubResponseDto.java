package com.coopang.hub.application.response.hub;

import com.coopang.apidata.application.address.Address;
import com.coopang.hub.domain.entity.hub.HubEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubResponseDto {
    private UUID hubId;
    private String hubName;
    private UUID hubManagerId;

    private Address address;

    private boolean isDeleted;

    @Builder
    private HubResponseDto(
        UUID hubId
        , String hubName
        , UUID hubManagerId
        , Address address
        , boolean isDeleted
    ) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.hubManagerId = hubManagerId;
        this.address = address;
        this.isDeleted = isDeleted;
    }

    /**
     * 오로지 하나의 객체만 반환하도록 하여 객체를 재사용해 메모리를 아끼도록 유도
     *
     * @param hub
     * @return
     */
    public static HubResponseDto fromHub(HubEntity hub) {
        return HubResponseDto
            .builder()
            .hubId(hub.getHubId())
            .hubName(hub.getHubName())
            .hubManagerId(hub.getHubManagerId())
            .address(new Address(
                hub.getAddressEntity().getZipCode()
                , hub.getAddressEntity().getAddress1()
                , hub.getAddressEntity().getAddress2()
            ))
            .isDeleted(hub.isDeleted())
            .build();
    }
}
