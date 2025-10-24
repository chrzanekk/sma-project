import {MenuItem} from "@/types/menu-item-types.ts";
import {AdminPanelView} from "@/components/admin/AdminPanel.tsx";

export const getAdminPanelMenuItems = (t: (key: string) => string, setActiveView: (view: AdminPanelView) => void): MenuItem[] => {
    return [
        {label: t('roles'), href: '#', onClick: () => setActiveView('roles'), value: 'roles'},
        {label: t('users'), href: '#', onClick: () => setActiveView('users'), value: 'users'},
        {label: t('companies'), href: '#', onClick: () => setActiveView('companies'), value: 'companies'},
        {label: t('positions'), href: '#', onClick: () => setActiveView('positions'), value: 'positions'},
    ];
}
