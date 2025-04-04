import React from 'react';
import {useTranslation} from 'react-i18next';
import {Box, Button, HStack, Text, VStack} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";
import {FiChevronDown} from "react-icons/fi";

const LanguageSwitcher: React.FC = () => {
    const {t, i18n} = useTranslation();
    const themeColors = useThemeColors();

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
                    bg={themeColors.bgColorPrimary}
                    ml={4}
                    color={themeColors.fontColor}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor,
                        color: themeColors.fontColorHover
                    }}
                    size="sm"
                    p={2}
                >
                    <HStack>
                        <VStack
                            display={{base: 'none', md: 'flex'}}
                            alignItems="flex-start"
                            gap="0px"
                            ml="2">
                            <Text fontSize="xs" lineHeight="1.2">
                                {t("currentLanguage")}
                            </Text>
                            <Text fontSize="xs" lineHeight="1.2">
                                {i18n.language === 'pl' ? t('polish') : t('english')}
                            </Text>
                        </VStack>

                        <Box display={{base: 'none', md: 'flex'}}>
                            <FiChevronDown/>
                        </Box>
                    </HStack>
                </Button>
            </MenuTrigger>
            <MenuContent bg={themeColors.bgColorSecondary} mt={2} p={1}>
                <MenuItem
                    bg={themeColors.bgColorSecondary}
                    p={2}
                    color={themeColors.fontColor}
                    rounded={'md'}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor,
                        color: themeColors.fontColorHover,
                    }}
                    value={'pl'}
                    valueText={'🇵🇱 Polski'}
                    onClick={() => changeLanguage('pl')}>
                    🇵🇱 Polski
                </MenuItem>
                <MenuItem
                    bg={themeColors.bgColorSecondary}
                    rounded={'md'}
                    color={themeColors.fontColor}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor,
                        color: themeColors.fontColorHover,
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
