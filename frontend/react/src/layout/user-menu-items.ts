import {useTranslation} from 'react-i18next'
import {useAuth} from "@/context/AuthContext.tsx";
import {UserMenuItem} from "@/types/menu-item-types.ts";

export const getUserMenuItems = (): UserMenuItem[] => {
    const {t} = useTranslation('userMenu');
    const {logOut} = useAuth();

    return [
        {label: t('profile'), href: '#'},
        {label: t('settings'), href: '#'},
        {label: t('adminPanel'), href: '#'},
        {label: t('logout'), href: '#', onClick: logOut},
    ];
};


