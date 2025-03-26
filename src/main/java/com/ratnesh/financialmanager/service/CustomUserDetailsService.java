package com.ratnesh.financialmanager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.model.Privilege;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;

@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                " ",  " ", true, true, true, true, getAuthorities(Arrays.asList(
                    roleRepository.findByName("ROLE_USER").orElse(null)
                ))
            );
        }

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), user.isActive(), true, true, true, getAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }

        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
