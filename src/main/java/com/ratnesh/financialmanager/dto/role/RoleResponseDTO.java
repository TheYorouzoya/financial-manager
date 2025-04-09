package com.ratnesh.financialmanager.dto.role;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class RoleResponseDTO {
    private UUID id;
    private String name;
    private List<String> privileges;
}
