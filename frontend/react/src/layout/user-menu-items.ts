import {useTranslation} from 'react-i18next'
import {useAuth} from "@/context/AuthContext.tsx";
import {MenuItem} from "@/types/menu-item-types.ts";
import {useNavigate} from "react-router-dom";
import {useResourcePermissions} from "@/context/ResourcePermissionContext.tsx";

export const getUserMenuItems = (): MenuItem[] => {
    const {t} = useTranslation('userMenu');
    const {logOut} = useAuth();
    const navigate = useNavigate();
    const {canAccessResource} = useResourcePermissions();

    const menuItems: MenuItem[] = [];

    menuItems.push({
        label: t('profile'),
        href: "#",
        onClick: () => navigate("/profile"),
        value: "profile"
    });

    // ✅ Admin Panel - check if user has access to ANY admin resource
    const hasAdminAccess =
        canAccessResource('USER_MANAGEMENT') ||
        canAccessResource('ROLE_MANAGEMENT') ||
        canAccessResource('COMPANY_MANAGEMENT') ||
        canAccessResource('POSITION_MANAGEMENT') ||
        canAccessResource('RESOURCE_MANAGEMENT');

    if (hasAdminAccess) {
        menuItems.push({
            label: t('adminPanel'),
            href: "#",
            value: "adminPanel",
            onClick: () => navigate('/adminPanel'),
        });
    }

    // ✅ Logout - always visible
    menuItems.push({
        label: t('logout'),
        href: '#',
        value: "logout",
        onClick: logOut
    });

    return menuItems;
};


