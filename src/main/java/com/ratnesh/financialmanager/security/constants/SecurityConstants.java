package com.ratnesh.financialmanager.security.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("Roles")
public final class SecurityConstants {
    
    // --- Role Definitions ---
    public static final String ROLE_SITE_ADMIN = "SITE_ADMIN";
    public static final String ROLE_FAMILY_HEAD = "FAMILY_HEAD";
    public static final String ROLE_FAMILY_ACCOUNTANT = "FAMILY_ACCOUNTANT";
    public static final String ROLE_FAMILY_MEMBER = "FAMILY_MEMBER";
    public static final String ROLE_FAMILY_CHILD = "FAMILY_CHILD";
    public static final String ROLE_USER = "USER";


    // --- Privilege Definitions ---
        
    // Global Privileges
    public static final String READ_ALL_FAMILIES = "READ_ALL_FAMILIES";
    public static final String MANAGE_ALL_USERS = "MANAGE_ALL_USERS";
    public static final String MANAGE_ROLES = "MANAGE_ROLES";
    public static final String MANAGE_PRIVILEGES = "MANAGE_PRIVILEGES";
    public static final String VIEW_SYSTEM_LOGS = "VIEW_SYSTEM_LOGS";
    
    // Family Asset Privileges
    public static final String FAMILY_CREATE_ASSET = "FAMILY_CREATE_ASSET";
    public static final String FAMILY_READ_ASSET = "FAMILY_READ_ASSET";
    public static final String FAMILY_UPDATE_ASSET = "FAMILY_UPDATE_ASSET";
    public static final String FAMILY_DELETE_ASSET = "FAMILY_DELETE_ASSET";

    // Family Account Privileges
    public static final String FAMILY_CREATE_ACCOUNT = "FAMILY_CREATE_ACCOUNT";
    public static final String FAMILY_READ_ACCOUNT = "FAMILY_READ_ACCOUNT";
    public static final String FAMILY_UPDATE_ACCOUNT = "FAMILY_UPDATE_ACCOUNT";
    public static final String FAMILY_DELETE_ACCOUNT = "FAMILY_DELETE_ACCOUNT";

    // Family Transaction Privileges
    public static final String FAMILY_CREATE_TRANSACTION = "FAMILY_CREATE_TRANSACTION";
    public static final String FAMILY_READ_TRANSACTION = "FAMILY_READ_TRANSACTION";
    public static final String FAMILY_UPDATE_TRANSACTION = "FAMILY_UPDATE_TRANSACTION";
    public static final String FAMILY_DELETE_TRANSACTION = "FAMILY_DELETE_TRANSACTION";

    // Family Document Privileges
    public static final String FAMILY_CREATE_DOCUMENT = "FAMILY_CREATE_DOCUMENT";
    public static final String FAMILY_READ_DOCUMENT = "FAMILY_READ_DOCUMENT";
    public static final String FAMILY_UPDATE_DOCUMENT = "FAMILY_UPDATE_DOCUMENT";
    public static final String FAMILY_DELETE_DOCUMENT = "FAMILY_DELETE_DOCUMENT";

    // Family Member Management Privileges
    public static final String FAMILY_ADD_MEMBER = "FAMILY_ADD_MEMBER";
    public static final String FAMILY_READ_MEMBER = "FAMILY_READ_MEMBER";
    public static final String FAMILY_UPDATE_MEMBER_ROLE = "FAMILY_UPDATE_MEMBER_ROLE";
    public static final String FAMILY_REMOVE_MEMBER = "FAMILY_REMOVE_MEMBER";

    // Family Alert Privileges
    public static final String FAMILY_READ_ALERT = "FAMILY_READ_ALERT";
    public static final String FAMILY_CONFIGURE_ALERT = "FAMILY_CONFIGURE_ALERT";

    // User Profile Privilege
    public static final String MANAGE_OWN_PROFILE = "MANAGE_OWN_PROFILE";

    private static final Map<String, List<String>> ROLE_PRIVILEGE_MAPPING;

    static {
        Map<String, List<String>> rolePrivilegeMap = new HashMap<>();

        // SITE_ADMIN: Has all permissions
        rolePrivilegeMap.put(ROLE_SITE_ADMIN, Arrays.asList(
            READ_ALL_FAMILIES,
            MANAGE_ALL_USERS,
            MANAGE_ROLES,
            MANAGE_PRIVILEGES,
            VIEW_SYSTEM_LOGS
        ));

        // FAMILY_HEAD: Full control within the family
        rolePrivilegeMap.put(ROLE_FAMILY_HEAD, Arrays.asList(
            FAMILY_CREATE_ASSET, FAMILY_READ_ASSET, FAMILY_UPDATE_ASSET, FAMILY_DELETE_ASSET,
            FAMILY_CREATE_ACCOUNT, FAMILY_READ_ACCOUNT, FAMILY_UPDATE_ACCOUNT, FAMILY_DELETE_ACCOUNT,
            FAMILY_CREATE_TRANSACTION, FAMILY_READ_TRANSACTION, FAMILY_UPDATE_TRANSACTION, FAMILY_DELETE_TRANSACTION,
            FAMILY_CREATE_DOCUMENT, FAMILY_READ_DOCUMENT, FAMILY_UPDATE_DOCUMENT, FAMILY_DELETE_DOCUMENT,
            FAMILY_ADD_MEMBER, FAMILY_READ_MEMBER, FAMILY_UPDATE_MEMBER_ROLE, FAMILY_REMOVE_MEMBER,
            FAMILY_READ_ALERT, FAMILY_CONFIGURE_ALERT,
            
            MANAGE_OWN_PROFILE
        ));

        // FAMILY_ACCOUNTANT: broad access to family financial data. partial asset management access
        rolePrivilegeMap.put(ROLE_FAMILY_ACCOUNTANT, Arrays.asList(
            FAMILY_CREATE_ASSET, FAMILY_READ_ASSET, FAMILY_UPDATE_ASSET, FAMILY_DELETE_ASSET,
            FAMILY_CREATE_ACCOUNT, FAMILY_READ_ACCOUNT, FAMILY_UPDATE_ACCOUNT, FAMILY_DELETE_ACCOUNT,
            FAMILY_READ_TRANSACTION, FAMILY_UPDATE_TRANSACTION,
            FAMILY_READ_DOCUMENT,
            FAMILY_READ_MEMBER, 
            FAMILY_READ_ALERT,
            MANAGE_OWN_PROFILE
        ));

        // FAMILY_MEMBER: can only view family-related data
        rolePrivilegeMap.put(ROLE_FAMILY_MEMBER, Arrays.asList(
            FAMILY_READ_ASSET,
            FAMILY_READ_ACCOUNT,
            FAMILY_READ_TRANSACTION, FAMILY_CREATE_TRANSACTION,
            FAMILY_READ_DOCUMENT,
            FAMILY_READ_MEMBER,
            FAMILY_READ_ALERT,
            MANAGE_OWN_PROFILE
        ));

        // FAMILY_CHILD: has a very limited view into the family data
        rolePrivilegeMap.put(ROLE_FAMILY_CHILD, Arrays.asList(
            FAMILY_READ_MEMBER,
            FAMILY_READ_ALERT,
            MANAGE_OWN_PROFILE
        ));

        // USER: default user role for the application
        rolePrivilegeMap.put(ROLE_USER, Arrays.asList(
            MANAGE_OWN_PROFILE
        ));

        ROLE_PRIVILEGE_MAPPING = rolePrivilegeMap;
    }

    public static Map<String, List<String>> getRolePrivilegeMapping() {
        return ROLE_PRIVILEGE_MAPPING;
    }

    // API URLs
    public static final String BASE_API_URL = "/api/v1";
    public static final String ACCOUNTS_URL = BASE_API_URL + "/accounts";
    public static final String ALERTS_URL = BASE_API_URL + "/alerts";
    public static final String ASSETS_URL = BASE_API_URL + "/assets";
    public static final String AUTH_URL = BASE_API_URL + "/auth";
    public static final String DOCUMENTS_URL = BASE_API_URL + "/documents";
    public static final String FAMILIES_URL = BASE_API_URL + "/families";
    public static final String PRIVILEGES_URL = BASE_API_URL + "/privileges";
    public static final String ROLES_URL = BASE_API_URL + "/roles";
    public static final String TRANSACTIONS_URL = BASE_API_URL + "/transactions";
    public static final String USERS_URL = BASE_API_URL + "/users";

    // Private Constructor
    private SecurityConstants() {}


}
