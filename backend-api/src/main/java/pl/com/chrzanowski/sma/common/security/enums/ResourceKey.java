package pl.com.chrzanowski.sma.common.security.enums;

import lombok.Getter;

import java.util.Set;

public enum ResourceKey {
    // Auth endpoints
    AUTH_PUBLIC(ResourceCode.AUTHENTICATION.getDisplayName(),
            ApiPath.AUTH + Constants.ALL,
            null,
            ResourceCode.AUTHENTICATION.getDescription(),
            true,
            Set.of()),

    // Account
    ACCOUNT_MANAGEMENT(ResourceCode.ACCOUNT.getDisplayName(),
            ApiPath.ACCOUNT + Constants.ALL,
            null,
            ResourceCode.ACCOUNT.getDescription(),
            false,
            Set.of()),

    // User Management
    USER_MANAGEMENT(ResourceCode.USER.getDisplayName(),
            ApiPath.USER + Constants.ALL,
            null,
            ResourceCode.USER.getDescription(),
            false,
            Set.of("ROLE_MANAGEMENT", "COMPANY_MANAGEMENT")),

    // Role Management
    ROLE_MANAGEMENT(ResourceCode.ROLE.getDisplayName(),
            ApiPath.ROLE + Constants.ALL,
            null,
            ResourceCode.ROLE.getDescription(),
            false,
            Set.of()),

    // Company Management
    COMPANY_MANAGEMENT(ResourceCode.COMPANY.getDisplayName(),
            ApiPath.COMPANY + Constants.ALL,
            null,
            ResourceCode.COMPANY.getDescription(),
            false,
            Set.of()),

    // Contractor Management
    CONTRACTOR_MANAGEMENT(ResourceCode.CONTRACTOR.getDisplayName(),
            ApiPath.CONTRACTOR + Constants.ALL,
            null, ResourceCode.CONTRACTOR.getDescription(),
            false,
            Set.of("CONTACT_MANAGEMENT")),

    // Contact Management
    CONTACT_MANAGEMENT(ResourceCode.CONTACT.getDisplayName(),
            ApiPath.CONTACT + Constants.ALL,
            null,
            ResourceCode.CONTACT.getDescription(),
            false,
            Set.of()),

    // Construction Site Management
    CONSTRUCTION_SITE_MANAGEMENT(ResourceCode.CONSTRUCTION_SITE.getDisplayName(),
            ApiPath.CONSTRUCTION_SITE + Constants.ALL,
            null,
            ResourceCode.CONSTRUCTION_SITE.getDescription(),
            false,
            Set.of("CONTRACT_MANAGEMENT", "CONTACT_MANAGEMENT", "CONTRACTOR_MANAGEMENT")),

    // Position Management
    POSITION_MANAGEMENT(ResourceCode.POSITION.getDisplayName(),
            ApiPath.POSITION + Constants.ALL,
            null,
            ResourceCode.POSITION.getDescription(),
            false,
            Set.of()),

    // Contract Management
    CONTRACT_MANAGEMENT(ResourceCode.CONTRACT.getDisplayName(),
            ApiPath.CONTRACT + Constants.ALL,
            null,
            ResourceCode.CONTRACT.getDescription(),
            false,
            Set.of("CONTRACTOR_MANAGEMENT", "CONTACT_MANAGEMENT")),

    // Resource Management (meta - zarządzanie samymi uprawnieniami)
    RESOURCE_MANAGEMENT(ResourceCode.RESOURCE.getDisplayName(),
            ApiPath.RESOURCE + Constants.ALL,
            null,
            ResourceCode.RESOURCE.getDescription(),
            false,
            Set.of()),
    // Employee Management (meta - zarządzanie pracownikami)
    EMPLOYEE_MANAGEMENT(ResourceCode.EMPLOYEE.getDisplayName(),
            ApiPath.EMPLOYEE + Constants.ALL,
            null,
            ResourceCode.EMPLOYEE.getDescription(),
            false,
            Set.of()),
    SCAFFOLDING_LOG_MANAGEMENT(ResourceCode.SCAFFOLDING_LOG.getDisplayName(),
            ApiPath.SCAFFOLDING_LOG + Constants.ALL,
            null,
            ResourceCode.SCAFFOLDING_LOG.getDescription(),
            false,
            Set.of("CONTRACTOR_MANAGEMENT", "CONTACT_MANAGEMENT", "CONSTRUCTION_SITE_MANAGEMENT", "SCAFFOLDING_LOG_POSITIONS")),
    SCAFFOLDING_LOG_POSITION_MANAGEMENT(ResourceCode.SCAFFOLDING_LOG_POSITIONS.getDisplayName(),
            ApiPath.SCAFFOLDING_LOG_POSITION + Constants.ALL,
            null,
            ResourceCode.SCAFFOLDING_LOG_POSITIONS.getDescription(),
            false,
            Set.of("SCAFFOLDING_LOG_MANAGEMENT"));

    @Getter
    private final String displayName;
    @Getter
    private final String endpointPattern;
    @Getter
    private final String httpMethod; // GET, POST, PUT, DELETE, null = all
    @Getter
    private final String description;
    @Getter
    private final boolean isPublic;
    @Getter
    private final Set<String> dependencies;

    ResourceKey(String displayName, String endpointPattern, String httpMethod, String description, boolean isPublic, Set<String> dependencies) {
        this.displayName = displayName;
        this.endpointPattern = endpointPattern;
        this.httpMethod = httpMethod;
        this.description = description;
        this.isPublic = isPublic;
        this.dependencies = dependencies;
    }

    private static class Constants {
        public static final String ALL = "/**";
    }
    }
