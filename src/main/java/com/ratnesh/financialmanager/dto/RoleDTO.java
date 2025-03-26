package com.ratnesh.financialmanager.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class RoleDTO {
    private UUID id;

    private String name;

    private List<UUID> users;
    private List<UUID> privileges;
}