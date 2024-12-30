import {useTranslation} from 'react-i18next'
import {useAuth} from "@/context/AuthContext.tsx";
import {MenuItem} from "@/types/menu-item-types.ts";

export const getUserMenuItems = (): MenuItem[] => {
    const {t} = useTranslation('userMenu');
    const {user, logOut} = useAuth();

    const hasRole = (role: string) => user?.roles?.includes(role);

    return [
        {label: t('profile'), href: '/profile'},
        ...(hasRole('ROLE_ADMIN') ? [{label: t('adminPanel'), href: '/adminPanel'}] :[]),
        {label: t('logout'), href: '#', onClick: logOut},
    ];
};


