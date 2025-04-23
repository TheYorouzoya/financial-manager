package com.ratnesh.financialmanager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
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
    
    @GetMapping
    public ResponseEntity<List<FamilyDTO>> getAllFamilies() {
        return ResponseEntity.ok(familyService.getAllFamilies());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FamilyDTO> getFamilyById(@PathVariable UUID id) {
        return familyService.getFamilyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<FamilyDTO> createFamily(@RequestBody FamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.createFamily(familyDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FamilyDTO> updateFamily(@PathVariable UUID id, @RequestBody FamilyDTO familyDTO) {
        return familyService.updateFamily(id, familyDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable UUID id) {
        familyService.deleteFamily(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<FamilyDTO> addFamilyMember(@PathVariable UUID id, @RequestBody AddMemberRequestDTO request) {
        return ResponseEntity.ok(familyService.addFamilyMember(id, request.getMemberId()));
        
    }
}
