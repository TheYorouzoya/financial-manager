package com.ratnesh.financialmanager.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.model.Privilege;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.PrivilegeRepository;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;
import com.ratnesh.financialmanager.security.constants.SecurityConstants;


@Component
public class seedRolesAndPrivilegeData implements ApplicationRunner {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        alreadySetup = (roleRepository.count() > 0);
        if (alreadySetup)
            return;

        Map<String, List<String>> rolePrivilegeMap = SecurityConstants.getRolePrivilegeMapping();

        for(Map.Entry<String, List<String>> entry : rolePrivilegeMap.entrySet()) {
            String roleName = entry.getKey();
            List<String> privilegeNames = entry.getValue();

            Set<Privilege> privileges = privilegeNames.stream()
                    .map(this::createPrivilegeIfNotFound)
                    .collect(Collectors.toSet());
            
            createRoleIfNotFound(roleName, privileges);
        }

        createAdminIfNotFound(roleRepository.findByName(SecurityConstants.ROLE_SITE_ADMIN).get());

        alreadySetup = true;
    }

    @Transactional
    User createAdminIfNotFound(Role adminRole) {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            User newAdmin = new User();
            newAdmin.setFirstName("Admin");
            newAdmin.setLastName("Test");
            newAdmin.setPassword(passwordEncoder.encode("onepassword"));
            newAdmin.setEmail("admin@financemanager.com");
            newAdmin.setUsername("admin");
            newAdmin.setRoles(Set.of(adminRole));
            newAdmin.setActive(true);
            userRepository.save(newAdmin);
            return newAdmin;
        }
        admin.setActive(true);
        return admin;
    }


    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name).orElse(null);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


    @Transactional
    Role createRoleIfNotFound(String name, Set<Privilege> privileges) {
        Role role = roleRepository.findByName(name).orElse(null);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }

        return role;
    }
}
