package com.ratnesh.financialmanager.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.family.FamilyDTO;
import com.ratnesh.financialmanager.exceptions.DuplicateResourceException;
import com.ratnesh.financialmanager.exceptions.ResourceNotFoundException;
import com.ratnesh.financialmanager.mapper.FamilyMapper;
import com.ratnesh.financialmanager.model.Family;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.FamilyRepository;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;
import com.ratnesh.financialmanager.security.constants.SecurityConstants;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FamilyService {
    
    private final FamilyRepository familyRepository;
    private final FamilyMapper familyMapper;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    
    public List<FamilyDTO> getAllFamilies() {
        List<Family> families = familyRepository.findAll();
        return families.stream().map(familyMapper::toDTO).toList();
    }
    
    public Optional<FamilyDTO> getFamilyById(UUID id) {
        return familyRepository.findById(id).map(familyMapper::toDTO);
    }

    public Optional<FamilyDTO> getFamilyByUserId(UUID userId) {
        return familyRepository.findByMembers_Id(userId).map(familyMapper::toDTO);
    }
    
    @Transactional
    public FamilyDTO createFamily(FamilyDTO familyDTO) {
        Family family = familyMapper.toEntity(familyDTO);
        Family savedFamily = familyRepository.save(family);
        return familyMapper.toDTO(savedFamily);
    }

    @Transactional
    public FamilyDTO createFamilyAsUser(FamilyDTO familyDTO, UUID userId) {
        // check if user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Family Creator User not found."));
        
        // check if user already has a family
        if (user.getFamily() != null) {
            throw new DuplicateResourceException("User already has a family.");
        }

        Set<User> familyMembers = new HashSet<>();
        familyMembers.add(user);

        Family family = familyMapper.toEntity(familyDTO);
        family.setMembers(familyMembers);

        Family savedFamily = familyRepository.save(family);
        user.setFamily(savedFamily);
        user.addRole(roleRepository.findByName(SecurityConstants.ROLE_FAMILY_HEAD).get());
        userRepository.save(user);

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
    public FamilyDTO addFamilyMember(UUID familyId, String requesterId, UUID memberId) {
        Family family = familyRepository.findById(familyId)
            .orElseThrow(() -> new EntityNotFoundException("Family not found."));
        User user = userRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("User not found."));

        if (user.getFamily() != null) {
            throw new DuplicateResourceException("Member is already part of a family");
        }

        User requestUser = userRepository.findById(UUID.fromString(requesterId))
            .orElseThrow(() -> new EntityNotFoundException("Requesting user does not exist"));

        if (!requestUser.getFamily().equals(family)) {
            throw new AccessDeniedException("Can only add member to requesting user's family");
        }

        user.setFamily(family);
        user.addRole(roleRepository.findByName(SecurityConstants.ROLE_FAMILY_MEMBER).get());
        family.addMember(user);

        return familyMapper.toDTO(family);
    }
    
}