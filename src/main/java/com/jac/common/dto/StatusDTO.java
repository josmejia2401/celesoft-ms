package com.jac.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {
    private Long id;
    private String category;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
}

