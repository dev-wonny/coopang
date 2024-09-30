package com.coopang.user.presentation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressRequestDto {
    private String zipCode;
    private String address1;
    private String address2;
}
