import i18n from 'i18next';
import ICU from 'i18next-icu';
import {initReactI18next} from 'react-i18next';
import commonEn from './en/common.json';
import commonPl from './pl/common.json';
import navbarEn from './en/navbar.json';
import navbarPl from './pl/navbar.json';
import userMenuEn from './en/userMenu.json';
import userMenuPl from './pl/userMenu.json';
import authEn from './en/auth.json';
import authPl from './pl/auth.json';
import footerEn from './en/footer.json'
import footerPl from './pl/footer.json'
import errorsEn from './en/errors.json'
import errorsPl from './pl/errors.json'
import adminPanelMenuPl from './pl/adminPanelMenu.json'
import adminPanelMenuEn from './en/adminPanelMenu.json'
import contractorsPl from './pl/contractors.json'
import contractorsEn from './en/contractors.json'
import countriesPl from './pl/countries.json'
import countriesEn from './en/countries.json'
import contactsPl from './pl/contacts.json'
import contactsEn from './en/contacts.json'
import companiesPl from './pl/companies.json'
import companiesEn from './en/companies.json'

i18n
    .use(initReactI18next)
    .use(ICU)
    .init({
    resources: {
        en: {
            common: commonEn,
            navbar: navbarEn,
            userMenu: userMenuEn,
            auth: authEn,
            footer: footerEn,
            errors: errorsEn,
            adminPanelMenu: adminPanelMenuEn,
            contractors: contractorsEn,
            countries: countriesEn,
            contacts: contactsEn,
            companies: companiesEn
        },
        pl: {
            common: commonPl,
            navbar: navbarPl,
            userMenu: userMenuPl,
            auth: authPl,
            footer: footerPl,
            errors: errorsPl,
            adminPanelMenu: adminPanelMenuPl,
            contractors: contractorsPl,
            countries: countriesPl,
            contacts: contactsPl,
            companies: companiesPl
        },
    },
    lng: 'pl',
    fallbackLng: 'en',
    ns: ['common', 'navbar', 'userMenu', 'auth', 'footer', 'adminPanelMenu',
        'contractors', 'countries','contacts','companies'],
    defaultNS: 'common',
    interpolation: {
        escapeValue: false,
    },
});

export default i18n;
