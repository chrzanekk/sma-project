import {useTranslation} from 'react-i18next'
import {useAuth} from "@/context/AuthContext.tsx";
import {MenuItem} from "@/types/menu-item-types.ts";
import {useNavigate} from "react-router-dom";

export const getUserMenuItems = (): MenuItem[] => {
    const {t} = useTranslation('userMenu');
    const {user, logOut} = useAuth();
    const navigate = useNavigate();

    const hasRole = (role: string) => user?.roles?.includes(role);

    return [
        {label: t('profile'), href: "#", onClick: (()=>navigate("/profile"))},
        ...(hasRole('ROLE_ADMIN') ? [{label: t('adminPanel'), href:"#" , onClick: (()=> navigate('/adminPanel'))}] :[]),
        {label: t('logout'), href: '#', onClick: logOut},
    ];
};


