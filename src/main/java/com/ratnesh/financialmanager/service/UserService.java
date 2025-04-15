package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.user.UserDTO;
import com.ratnesh.financialmanager.dto.user.UserRegistrationDTO;
import com.ratnesh.financialmanager.dto.user.UserResponseDTO;
import com.ratnesh.financialmanager.exceptions.DuplicateResourceException;
import com.ratnesh.financialmanager.exceptions.InvalidUsernameException;
import com.ratnesh.financialmanager.mapper.UserMapper;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;
import com.ratnesh.financialmanager.config.CacheConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
    
    @Cacheable(value = CacheConfig.USER_CACHE_NAME)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }

    public UserResponseDTO createUser(UserRegistrationDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        String username = userDTO.getUsername();
        
        if (!pattern.matcher(username).matches()) {
            throw new InvalidUsernameException("Username can only contain letters, numbers, and underscores.");
        }
        
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username is already in use.");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setActive(true);
        user.setRoles(Set.of(roleRepository.findByName("ROLE_USER").get()));

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(savedUser);
    }
    
    public Optional<UserDTO> getUserById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @CachePut(value = CacheConfig.USER_CACHE_NAME, key = "#id")
    @Transactional
    public Optional<UserResponseDTO> updateUser(UUID id, UserDTO userDTO) {
        return userRepository.findById(id).map(existingUser -> {
            User updatedUser = userMapper.toEntity(userDTO);
            updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            updatedUser = userRepository.save(updatedUser);
            return userMapper.toUserResponseDTO(updatedUser);
        });
    }
    
    @CacheEvict(value = CacheConfig.USER_CACHE_NAME, key = "#id")
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
