import {useTranslation} from "react-i18next";

export interface NavItem {
    label: string;
    subLabel?: string;
    children?: Array<NavItem>;
    href?: string;
}

export const getNavItems = (): Array<NavItem> => {
    const {t} = useTranslation('navbar');
    return [
        {
            label: t('dashboard'),
            href: '/dashboard'
        },
        {
            label: t('diary'),
            children: [
                {
                    label: t('diaryList'),
                    subLabel: t('diaryListSubLabel'),
                    href: '#',
                },
                {
                    label: t('diaryAddNew'),
                    subLabel: t('diaryAddNewSubLabel'),
                    href: '#',
                },
            ],
        },
        {
            label: t('warehouse'),
            children: [
                {
                    label: t('mainWarehouse'),
                    subLabel: t('mainWarehouseSubLabel'),
                    href: '#',
                },
                {
                    label: t('constructionSites'),
                    subLabel: t('constructionSitesSubLabel'),
                    href: '#',
                },
            ],
        },
        {
            label: t('contractors'),
            href: '/contractors',
        }
    ];
}
