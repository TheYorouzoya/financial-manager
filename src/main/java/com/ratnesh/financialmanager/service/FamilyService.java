package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.family.FamilyDTO;
import com.ratnesh.financialmanager.mapper.FamilyMapper;
import com.ratnesh.financialmanager.model.Family;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.FamilyRepository;
import com.ratnesh.financialmanager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FamilyService {
    
    private final FamilyRepository familyRepository;
    private final FamilyMapper familyMapper;

    private final UserRepository userRepository;
    
    public List<FamilyDTO> getAllFamilies() {
        List<Family> families = familyRepository.findAll();
        return families.stream().map(familyMapper::toDTO).toList();
    }
    
    public Optional<FamilyDTO> getFamilyById(UUID id) {
        return familyRepository.findById(id).map(familyMapper::toDTO);
    }
    
    @Transactional
    public FamilyDTO createFamily(FamilyDTO familyDTO) {
        Family family = familyMapper.toEntity(familyDTO);
        Family savedFamily = familyRepository.save(family);
        return familyMapper.toDTO(savedFamily);
    }
    
    @Transactional
    public Optional<FamilyDTO> updateFamily(UUID id, FamilyDTO familyDTO) {
        return familyRepository.findById(id).map(existingFamily -> {
            existingFamily.setName(familyDTO.getName());
            Family updatedFamily = familyRepository.save(existingFamily);
            return familyMapper.toDTO(updatedFamily);
        });
    }
    
    @Transactional
    public void deleteFamily(UUID id) {
        familyRepository.deleteById(id);
    }

    @Transactional
    public FamilyDTO addFamilyMember(UUID id, UUID memberId) {
        Family family = familyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Family not found."));
        User user = userRepository.getReferenceById(memberId);
        
        family.getMembers().add(user);
        return familyMapper.toDTO(familyRepository.save(family));
    }
}