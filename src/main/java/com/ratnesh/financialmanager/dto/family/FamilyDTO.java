package com.ratnesh.financialmanager.dto.family;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class FamilyDTO {
    private UUID id;

    @NotEmpty(message = "Family name is required.")
    private String name;
    private String description;
    private Set<UUID> members;
}
