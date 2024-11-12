export interface NavItem {
    label: string;
    subLabel?: string;
    children?: Array<NavItem>;
    href?: string;
}

export const NAV_ITEMS: Array<NavItem> = [
    {
        label: 'Dziennik rusztowań',
        children: [
            {
                label: 'Lista dzienników',
                subLabel: 'Przeglądaj utworzone dzienniki rusztowań',
                href: '#',
            },
            {
                label: 'Dodaj nowy',
                subLabel: 'Stworzenie nowego dziennika rusztowań',
                href: '#',
            },
        ],
    },
    {
        label: 'Magazyn',
        children: [
            {
                label: 'Główny',
                subLabel: 'Magazyn główny',
                href: '#',
            },
            {
                label: 'Budowy',
                subLabel: 'Przeglądaj stany magazynowe na budowach',
                href: '#',
            },
        ],
    },
];
