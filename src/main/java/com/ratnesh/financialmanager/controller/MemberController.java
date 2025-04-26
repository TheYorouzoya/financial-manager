package com.ratnesh.financialmanager.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratnesh.financialmanager.dto.role.FamilyMemberRoleUpdateDTO;
import com.ratnesh.financialmanager.security.constants.SecurityConstants;
import com.ratnesh.financialmanager.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(SecurityConstants.MEMBERS_URL)
@RequiredArgsConstructor
public class MemberController {
    
    private final RoleService roleService;

    @PreAuthorize("hasRole(@Roles.FAMILY_HEAD)")
    @PostMapping("/role")
    public ResponseEntity<Void> addFamilyMemberRole(
        @AuthenticationPrincipal Jwt jwtToken, 
        @Valid @RequestBody FamilyMemberRoleUpdateDTO roleUpdateDTO
    ) {    
        
        UUID requestingUser = UUID.fromString(jwtToken.getClaim("userId"));
        roleService.addFamilyMemberRole(
            requestingUser, 
            roleUpdateDTO.getMemberId(), 
            roleUpdateDTO.getRole()
        );

        return ResponseEntity.ok().build();
    } 
}
