import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import commonEn from './en/common.json';
import commonPl from './pl/common.json';
import navbarEn from './en/navbar.json';
import navbarPl from './pl/navbar.json';
import userMenuEn from './en/userMenu.json';
import userMenuPl from './pl/userMenu.json';
import authEn from './en/auth.json';
import authPl from './pl/auth.json';

i18n.use(initReactI18next).init({
    resources: {
        en: {
            common: commonEn,
            navbar: navbarEn,
            userMenu: userMenuEn,
            auth: authEn,
        },
        pl: {
            common: commonPl,
            navbar: navbarPl,
            userMenu: userMenuPl,
            auth: authPl
        },
    },
    lng: 'pl',
    fallbackLng: 'en',
    ns: ['common', 'navbar', 'userMenu', 'auth`'], // Definiujemy przestrzenie nazw (namespaces)
    defaultNS: 'common',
    interpolation: {
        escapeValue: false,
    },
});

export default i18n;
