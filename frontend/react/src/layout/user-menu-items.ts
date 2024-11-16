import {useTranslation} from 'react-i18next'

export const getUserMenuItems = () => {
    const {t} = useTranslation('userMenu');

    return [
        {label: t('profile'), href: '#'},
        {label: t('settings'), href: '#'},
        {label: t('adminPanel'), href: '#'},
        {label: t('logout'), href: '#'},
    ];
};


