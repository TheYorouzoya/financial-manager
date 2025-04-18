package com.ratnesh.financialmanager.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.model.Privilege;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.repository.PrivilegeRepository;
import com.ratnesh.financialmanager.repository.RoleRepository;
import com.ratnesh.financialmanager.repository.UserRepository;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

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
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Set<Privilege> adminPrivileges = Set.of(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Set.of(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        createAdminIfNotFound(adminRole);
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
