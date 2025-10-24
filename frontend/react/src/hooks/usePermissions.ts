// hooks/usePermissions.ts
import {useAuth} from "@/context/AuthContext.tsx";
import {useResourcePermissions} from "@/context/ResourcePermissionContext.tsx";

export const usePermissions = () => {
    const {user} = useAuth();
    const {canAccessResource, getUserResources} = useResourcePermissions();

    const hasRole = (roleName: string): boolean => {
        if (!user?.roles) return false;
        return user.roles.some(role => role === roleName);
    };

    const hasAnyRole = (roleNames: string[]): boolean => {
        if (!user?.roles) return false;
        return user.roles.some(role => roleNames.includes(role));
    };

    const hasAllRoles = (roleNames: string[]): boolean => {
        if (!user?.roles) return false;
        return roleNames.every(roleName =>
            user.roles?.some(role => role === roleName)
        );
    };

    // ✅ Dynamic checks based on backend resources
    const canAccess = {
        account: () => canAccessResource('ACCOUNT_MANAGEMENT'),
        users: () => canAccessResource('USER_MANAGEMENT'),
        roles: () => canAccessResource('ROLE_MANAGEMENT'),
        companies: () => canAccessResource('COMPANY_MANAGEMENT'),
        contractors: () => canAccessResource('CONTRACTOR_MANAGEMENT'),
        contacts: () => canAccessResource('CONTACT_MANAGEMENT'),
        constructionSites: () => canAccessResource('CONSTRUCTION_SITE_MANAGEMENT'),
        positions: () => canAccessResource('POSITION_MANAGEMENT'),
        contracts: () => canAccessResource('CONTRACT_MANAGEMENT'),
        resources: () => canAccessResource('RESOURCE_MANAGEMENT'),
    };


    return {
        hasRole,
        hasAnyRole,
        hasAllRoles,
        canAccessResource,
        canAccess,
        getUserResources
    };
};
