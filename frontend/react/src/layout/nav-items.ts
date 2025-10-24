import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {NavItem} from "@/types/nav-item-types.ts";
import {useResourcePermissions} from "@/context/ResourcePermissionContext.tsx";

export const getNavItems = (): NavItem[] => {
    const {t} = useTranslation('navbar');
    const navigate = useNavigate();
    const {canAccessResource, loading} = useResourcePermissions();

    // Return empty array while loading to prevent flashing incorrect menu
    if (loading) {
        return [];
    }

    const navItems: NavItem[] = [];


    // Dashboard - always visible for authenticated users
    navItems.push({
        label: t('dashboard'),
        href: '#',
        onClick: () => navigate('/dashboard'),
        value: "dashboard"
    });

    // Diary - always visible (not protected by resources yet)
    navItems.push({
        label: t('diary'),
        value: "diary",
        children: [
            {
                label: t('diaryList'),
                href: '#',
                onClick: () => navigate('/diaryList'),
                value: "diaryList"
            },
            {
                label: t('diaryAddNew'),
                href: '#',
                onClick: () => navigate('/diaryAddNew'),
                value: "diaryAddNew"
            },
        ],
    });

    // Warehouse - always visible (not protected by resources yet)
    navItems.push({
        label: t('warehouse'),
        value: "warehouse",
        children: [
            {
                label: t('mainWarehouse'),
                href: '#',
                onClick: () => navigate('/mainWarehouse'),
                value: "mainWarehouse"
            }
        ]
    });

    // ✅ Contractors - dynamic permission check
    if (canAccessResource('CONTRACTOR_MANAGEMENT')) {
        navItems.push({
            label: t('contractors'),
            href: '#',
            onClick: () => navigate('/contractors'),
            value: "contractors"
        });
    }

    // ✅ Contacts - dynamic permission check
    if (canAccessResource('CONTACT_MANAGEMENT')) {
        navItems.push({
            label: t('contacts'),
            href: '#',
            onClick: () => navigate('/contacts'),
            value: "contacts"
        });
    }

    // ✅ Construction Sites - dynamic permission check
    if (canAccessResource('CONSTRUCTION_SITE_MANAGEMENT')) {
        navItems.push({
            label: t('constructionSites'),
            href: '#',
            onClick: () => navigate('/constructionSites'),
            value: "constructionSites"
        });
    }

    // ✅ Contracts - dynamic permission check
    if (canAccessResource('CONTRACT_MANAGEMENT')) {
        navItems.push({
            label: t('contracts'),
            href: '#',
            onClick: () => navigate('/contracts'),
            value: "contracts"
        });
    }

    return navItems;
}
