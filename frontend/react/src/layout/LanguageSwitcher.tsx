import React from 'react';
import {useTranslation} from 'react-i18next';
import {Button, Menu, MenuButton, MenuItem, MenuList} from "@chakra-ui/react";
import {ChevronDownIcon} from "@chakra-ui/icons";
import {themeColors} from "@/theme/theme-colors.ts";

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
                bg={themeColors.bgColor()}
                ml={4}
                color={themeColors.fontColor()}
                _hover={{
                    textDecoration: 'none',
                    bg: themeColors.highlightBgColor(),
                    color: themeColors.popoverBgColor()
                }}
                as={Button}
                rightIcon={<ChevronDownIcon/>}
                variant="outline"
                size="sm"
                p={2}
            >
                {i18n.language === 'pl' ? 'Polski' : 'English'}
            </MenuButton>
            <MenuList bg={themeColors.bgColor()} mt={4} p={1}>
                <MenuItem
                    bg={themeColors.bgColor()}
                    p={2}
                    color={themeColors.fontColor()}
                    rounded={'md'}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor(),
                        color: themeColors.popoverBgColor(),
                        border: '1px solid white'
                    }}
                    onClick={() => changeLanguage('pl')}>🇵🇱 Polski</MenuItem>
                <MenuItem
                    bg={themeColors.bgColor()}
                    rounded={'md'}
                    color={themeColors.fontColor()}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor(),
                        color: themeColors.popoverBgColor(),
                        border: '1px solid white'
                    }}
                    onClick={() => changeLanguage('en')}>🇬🇧 English</MenuItem>
            </MenuList>
        </Menu>
    );
};

export default LanguageSwitcher;
