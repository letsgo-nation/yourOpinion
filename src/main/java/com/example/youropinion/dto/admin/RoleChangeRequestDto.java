package com.example.youropinion.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleChangeRequestDto {
    private boolean admin;
    private String adminToken;
}
