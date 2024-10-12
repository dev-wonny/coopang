package com.coopang.apidata.application.company.response;

import com.coopang.apidata.application.address.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private UUID companyId;
    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;

    private Address address;

    private boolean isDeleted;
}
