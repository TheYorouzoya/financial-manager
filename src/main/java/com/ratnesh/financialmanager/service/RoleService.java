package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.role.RoleDTO;
import com.ratnesh.financialmanager.exceptions.BadRoleRequestException;
import com.ratnesh.financialmanager.exceptions.ResourceNotFoundException;
import com.ratnesh.financialmanager.mapper.RoleMapper;
import com.ratnesh.financialmanager.model.Family;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;
import com.ratnesh.financialmanager.security.constants.SecurityConstants;
import com.ratnesh.financialmanager.config.CacheConfig;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    private final UserRepository userRepository;

    @Cacheable(value = CacheConfig.ROLE_CACHE_NAME)
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toDTO).toList();
    }

    @Cacheable(value = CacheConfig.ROLE_CACHE_NAME, key = "#id")
    public Optional<RoleDTO> getRoleById(UUID id) {
        return roleRepository.findById(id).map(roleMapper::toDTO);
    }

    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    @CachePut(value = CacheConfig.ROLE_CACHE_NAME, key = "#id")
    @Transactional
    public Optional<RoleDTO> updateRole(UUID id, RoleDTO roleDTO) {
        return roleRepository.findById(id).map(existingRole -> {
            existingRole.setName(roleDTO.getName());
            Role updatedRole = roleRepository.save(existingRole);
            return roleMapper.toDTO(updatedRole);
        });
    }

    @CacheEvict(value = CacheConfig.ROLE_CACHE_NAME, key = "#id")
    @Transactional
    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    public RoleDTO addUserRole(UUID id, UUID userId) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found."));
        User user = userRepository.getReferenceById(userId);

        role.getUsers().add(user);
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Transactional
    public void addFamilyMemberRole(UUID requester, UUID memberId, String roleToAdd) {
        User requestingUser = userRepository.findById(requester)
                .orElseThrow(() -> new ResourceNotFoundException("Requesting user does not exist"));
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Requested member does not exist"));
        
        Family memberFamily = member.getFamily();

        if (memberFamily == null) {
            throw new BadRoleRequestException("Member not part of requesting user's family");
        }

        Family requestFamily = requestingUser.getFamily();

        if (requestFamily == null) {
            throw new ResourceNotFoundException("Requesting user does not have a family");
        }

        if (!memberFamily.equals(requestFamily)) {
            throw new BadRoleRequestException("Member not part of requesting user's family");
        }

        if (!roleToAdd.startsWith(SecurityConstants.FAMILY_ROLE_PREFIX)) {
            throw new BadRoleRequestException("Given role is not a family role");
        }

        Role role = roleRepository.findByName(roleToAdd)
            .orElseThrow(() -> new BadRoleRequestException("Given role does not exist"));

        member.addRole(role);
        role.addUser(member);
    }
}