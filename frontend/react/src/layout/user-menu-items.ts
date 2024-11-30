import {useTranslation} from 'react-i18next'
import {useAuth} from "@/context/AuthContext.tsx";
import {UserMenuItem} from "@/types/menu-item-types.ts";

export const getUserMenuItems = (): UserMenuItem[] => {
    const {t} = useTranslation('userMenu');
    const {user, logOut} = useAuth();

    const hasRole = (role: string) => user?.roles?.includes(role);

    return [
        {label: t('profile'), href: '#'},
        {label: t('settings'), href: '#'},
        ...(hasRole('ROLE_ADMIN') ? [{label: t('adminPanel'), href: '#'}] :[]),
        {label: t('logout'), href: '#', onClick: logOut},
    ];
};


