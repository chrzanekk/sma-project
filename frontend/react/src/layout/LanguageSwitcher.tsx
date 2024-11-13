import React from 'react';
import {useTranslation} from 'react-i18next';
import {Button, Menu, MenuButton, MenuItem, MenuList} from "@chakra-ui/react";
import {ChevronDownIcon} from "@chakra-ui/icons";

const LanguageSwitcher: React.FC = () => {
    const {i18n} = useTranslation();
    const changeLanguage = (lang: string): void => {
        i18n.changeLanguage(lang).then(() => {
            console.log(`Język zmieniony na: ${lang}`);
        }).catch((error) => {
            console.error('Błąd podczas zmiany języka:', error);
        });
    };

    return (
        <Menu>
            <MenuButton
                as={Button}
                rightIcon={<ChevronDownIcon/>}
                variant="outline"
                size="sm"
            >
                {i18n.language === 'pl' ? 'Polski' : 'English'}
            </MenuButton>
            <MenuList>
                <MenuItem onClick={() => changeLanguage('pl')}>🇵🇱 Polski</MenuItem>
                <MenuItem onClick={() => changeLanguage('en')}>🇬🇧 English</MenuItem>
            </MenuList>
        </Menu>
    );
};

export default LanguageSwitcher;
