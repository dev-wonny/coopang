package com.coopang.apidata.application.address;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Address {
    private String zipCode;
    private String address1;
    private String address2;

    public Address(String zipCode, String address1, String address2) {
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
    }
}
