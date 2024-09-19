package com.coopang.gateway.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HeaderResponseDto {
    private String token;
    private String userId;
    private String role;
}
