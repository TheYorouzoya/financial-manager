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
import com.ratnesh.financialmanager.mapper.RoleMapper;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;
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
}