package com.ratnesh.financialmanager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratnesh.financialmanager.dto.family.AddMemberRequestDTO;
import com.ratnesh.financialmanager.dto.family.FamilyDTO;
import com.ratnesh.financialmanager.security.constants.SecurityConstants;
import com.ratnesh.financialmanager.service.FamilyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(SecurityConstants.FAMILIES_URL)
@RequiredArgsConstructor
public class FamilyController {
    
    private final FamilyService familyService;
    
    @PreAuthorize("hasAuthority(@Roles.READ_ALL_FAMILIES)")
    @GetMapping
    public ResponseEntity<List<FamilyDTO>> getAllFamilies() {
        return ResponseEntity.ok(familyService.getAllFamilies());
    }
    
    @PreAuthorize("hasAuthority(@Roles.READ_ALL_FAMILIES)")
    @GetMapping("/{id}")
    public ResponseEntity<FamilyDTO> getFamilyById(@PathVariable UUID id) {
        return familyService.getFamilyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthoriy(@Roles.FAMILY_READ_MEMBER)")
    @GetMapping("/me")
    public ResponseEntity<FamilyDTO> getMyFamily(@AuthenticationPrincipal Jwt jwtToken) {
        UUID userId = jwtToken.getClaim("userId");
        return familyService.getFamilyByUserId(userId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasAuthority(@Roles.ROLE_SITE_ADMIN) or !hasAuthority(@Roles.ROLE_FAMILY_CHILD)")
    @PostMapping
    public ResponseEntity<FamilyDTO> createFamily(@RequestBody FamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.createFamily(familyDTO));
    }
    
    @PreAuthorize("hasAuthority(@Roles.ROLE_FAMILY_HEAD)")
    @PutMapping("/{id}")
    public ResponseEntity<FamilyDTO> updateFamily(@PathVariable UUID id, @RequestBody FamilyDTO familyDTO) {
        return familyService.updateFamily(id, familyDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasAuthority(@Roles.ROLE_FAMILY_HEAD)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable UUID id) {
        familyService.deleteFamily(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Roles.FAMILY_ADD_MEMBER)")
    @PostMapping("/{id}/members")
    public ResponseEntity<FamilyDTO> addFamilyMember(@PathVariable UUID id, @RequestBody AddMemberRequestDTO request) {
        return ResponseEntity.ok(familyService.addFamilyMember(id, request.getMemberId()));
        
    }
}
