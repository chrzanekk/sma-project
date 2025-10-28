// hooks/useAdminPanelMenu.ts
import {useMemo} from 'react';
import {MenuItem} from "@/types/menu-item-types.ts";
import {AdminPanelView} from "@/components/admin/AdminPanel.tsx";
import {useResourcePermissions} from "@/context/ResourcePermissionContext.tsx";

export const useAdminPanelMenu = (
    t: (key: string) => string,
    setActiveView: (view: AdminPanelView) => void
): MenuItem[] => {
    const {canAccessResource} = useResourcePermissions();

    return useMemo(() => {
        const menuItems: MenuItem[] = [];

        // ✅ Users - dynamic check
        if (canAccessResource('USER_MANAGEMENT')) {
            menuItems.push({
                label: t('users'),
                href: '#',
                onClick: () => setActiveView('users'),
                value: 'users'
            });
        }

        // ✅ Roles - dynamic check
        if (canAccessResource('ROLE_MANAGEMENT')) {
            menuItems.push({
                label: t('roles'),
                href: '#',
                onClick: () => setActiveView('roles'),
                value: 'roles'
            });
        }

        // ✅ Companies - dynamic check
        if (canAccessResource('COMPANY_MANAGEMENT')) {
            menuItems.push({
                label: t('companies'),
                href: '#',
                onClick: () => setActiveView('companies'),
                value: 'companies'
            });
        }

        // ✅ Positions - dynamic check
        if (canAccessResource('POSITION_MANAGEMENT')) {
            menuItems.push({
                label: t('positions'),
                href: '#',
                onClick: () => setActiveView('positions'),
                value: 'positions'
            });
        }

        // ✅ Permissions - dynamic check
        if (canAccessResource('RESOURCE_MANAGEMENT')) {
            menuItems.push({
                label: t('permissions'),
                href: '#',
                onClick: () => setActiveView('permissions'),
                value: 'permissions'
            });
        }

        return menuItems;
    }, [t, setActiveView, canAccessResource]);
};
