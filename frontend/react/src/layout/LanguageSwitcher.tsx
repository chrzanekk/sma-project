import React from 'react';
import {useTranslation} from 'react-i18next';
import {Button} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";
import {FaChevronDown} from "react-icons/fa6";

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
        <MenuRoot>
            <MenuTrigger asChild>
                <Button
                    bg={themeColors.bgColorPrimary()}
                    ml={4}
                    color={themeColors.fontColor()}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor(),
                        color: themeColors.fontColorHover()
                    }}
                    size="sm"
                    p={2}
                >
                    <FaChevronDown/>
                    {i18n.language === 'pl' ? 'Polski' : 'English'}
                </Button>
            </MenuTrigger>
            <MenuContent bg={themeColors.bgColorSecondary()} mt={2} p={1}>
                <MenuItem
                    bg={themeColors.bgColorSecondary()}
                    p={2}
                    color={themeColors.fontColor()}
                    rounded={'md'}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor(),
                        color: themeColors.fontColorHover(),
                    }}
                    value={'pl'}
                    valueText={'🇵🇱 Polski'}
                    onClick={() => changeLanguage('pl')}>
                    🇵🇱 Polski
                </MenuItem>
                <MenuItem
                    bg={themeColors.bgColorSecondary()}
                    rounded={'md'}
                    color={themeColors.fontColor()}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor(),
                        color: themeColors.fontColorHover(),
                    }}
                    value={'en'}
                    valueText={'🇬🇧 English'}
                    onClick={() => changeLanguage('en')}>
                    🇬🇧 English
                </MenuItem>
            </MenuContent>
        </MenuRoot>
    );
};

export default LanguageSwitcher;
