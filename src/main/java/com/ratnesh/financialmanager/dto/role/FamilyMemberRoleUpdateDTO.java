package com.ratnesh.financialmanager.dto.role;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FamilyMemberRoleUpdateDTO {
    @NotNull(message = "Member ID cannot be blank")
    private UUID memberId;

    @NotBlank(message = "Must provide a role to update")
    private String role;    
}
