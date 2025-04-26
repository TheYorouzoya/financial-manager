package com.ratnesh.financialmanager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/me")
    public ResponseEntity<FamilyDTO> getMyFamily(@AuthenticationPrincipal Jwt jwtToken) {
        UUID userId = UUID.fromString(jwtToken.getClaim("userId"));
        return familyService.getFamilyByUserId(userId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("!hasRole(@Roles.FAMILY_CHILD)")
    @PostMapping
    public ResponseEntity<FamilyDTO> createFamily(@RequestBody FamilyDTO familyDTO, @AuthenticationPrincipal Jwt jwtToken) {
        UUID userId = UUID.fromString(jwtToken.getClaim("userId"));
        FamilyDTO newFamily = familyService.createFamilyAsUser(familyDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFamily);
    }
    
    @PreAuthorize("hasRole(@Roles.FAMILY_HEAD)")
    @PutMapping("/{id}")
    public ResponseEntity<FamilyDTO> updateFamily(@PathVariable UUID id, @RequestBody FamilyDTO familyDTO) {
        return familyService.updateFamily(id, familyDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasRole(@Roles.FAMILY_HEAD)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable UUID id) {
        familyService.deleteFamily(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole(@Roles.FAMILY_HEAD)")
    @PostMapping("/{familyId}/members")
    public ResponseEntity<FamilyDTO> addFamilyMember(
        @PathVariable UUID familyId,
        @AuthenticationPrincipal Jwt jwtToken,
        @RequestBody AddMemberRequestDTO request
    ) {

        return ResponseEntity.ok(
            familyService.addFamilyMember(
                familyId, 
                jwtToken.getClaim("userId"), 
                request.getMemberId()
            )
        ); 
    }
}
