import {useTranslation} from "react-i18next";
import {MenuItem} from "@/types/menu-item-types.ts";
import {AdminPanelView} from "@/components/admin/AdminPanel.tsx";

export const getAdminPanelMenuItems = (setActiveView: (view: AdminPanelView) => void): MenuItem[] => {
    const {t} = useTranslation('adminPanelMenu');
    return [
        {label: t('roles'), href: '#', onClick: () => setActiveView('roles')},
        {label: t('users'), href: '#', onClick: () => setActiveView('users')},
    ];
}
