package com.coopang.user.application.request;

import lombok.Data;

@Data
public class AddressDto {
    private String zipCode;
    private String address1;
    private String address2;
}