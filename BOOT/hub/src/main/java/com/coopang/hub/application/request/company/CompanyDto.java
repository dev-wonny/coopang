package com.coopang.hub.application.request.company;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDto {
    private UUID companyId;

    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;

    private String zipCode;
    private String address1;
    private String address2;
}