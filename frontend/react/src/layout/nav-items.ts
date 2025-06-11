import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {NavItem} from "@/types/nav-item-types.ts";

export const getNavItems = (): NavItem[] => {
    const {t} = useTranslation('navbar');
    const navigate = useNavigate();
    return [
        {
            label: t('dashboard'),
            href: '#',
            onClick: (() => navigate('/dashboard')),
            value: "dashboard"
        },
        {
            label: t('diary'),
            value: "diary",
            children: [
                {
                    label: t('diaryList'),
                    href: '#',
                    onClick: (() => navigate('/diaryList')),
                    value: "diaryList"
                },
                {
                    label: t('diaryAddNew'),
                    href: '#',
                    onClick: (() => navigate('/diaryAddNew')),
                    value: "diaryAddNew"
                },
            ],
        },
        {
            label: t('warehouse'),
            value: "warehouse",
            children: [
                {
                    label: t('mainWarehouse'),
                    href: '#',
                    onClick: (() => navigate('/mainWarehouse')),
                    value: "mainWarehouse"
                }
            ]
        },
        {
            label: t('contractors'),
            href: '#',
            onClick: (() => navigate('/contractors')),
            value: "contractors"
        },
        {
            label: t('contacts'),
            href: '#',
            onClick: (() => navigate('/contacts')),
            value: "contacts"
        },
        {
            label: t('constructionSites'),
            href: '#',
            onClick: (() => navigate('/constructionSites')),
            value: "constructionSites"
        },
    ];
}
