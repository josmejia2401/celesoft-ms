package com.jac.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityOptionsDTO {
    private Long userId;
    private Long tokenId;
    private String audience;
    private String appName;
    private String role;
}
