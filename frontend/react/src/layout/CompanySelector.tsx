import React, {useEffect, useState} from "react";
import {Box, HStack, Text, VStack} from "@chakra-ui/react";
import useUser from "@/hooks/UseUser";
import {CompanyBaseDTO} from "@/types/company-type"; // Typ firmy
import {Button} from "@/components/ui/button.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FiChevronDown} from "react-icons/fi";
import {useTranslation} from "react-i18next";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";


const CompanySelector: React.FC = () => {
    const {user} = useUser();
    const {t} = useTranslation('navbar');
    const themeColors = useThemeColors();
    const [selectedCompany, setSelectedCompany] = useState<CompanyBaseDTO | null>(null);

    // Przy inicjalizacji sprawdzamy, czy w localStorage jest już wybrana firma.
    useEffect(() => {
        const storedCompany = localStorage.getItem("selectedCompany");
        if (storedCompany) {
            setSelectedCompany(JSON.parse(storedCompany));
        }
    }, []);

    const handleSelectCompany = (company: CompanyBaseDTO) => {
        setSelectedCompany(company);
        localStorage.setItem("selectedCompany", JSON.stringify(company));
    };

    // Lista firm przypisanych do użytkownika (jeżeli user nie jest zalogowany lub nie ma firm, może być pusta)
    const companies: CompanyBaseDTO[] = user?.companies || [];

    return (
        <MenuRoot>
            <MenuTrigger asChild>
                <Button bg={themeColors.buttonBgColor}
                        ml={4}
                        color={themeColors.fontColor}
                        _hover={{
                            textDecoration: 'none',
                            bg: themeColors.highlightBgColor,
                            color: themeColors.fontColorHover
                        }}
                        transition="all 0.3s"
                        variant="solid"
                        size="sm"
                        p={2}>
                    <HStack>
                        <VStack display={{base: 'none', md: 'flex'}}
                                alignItems="flex-start"
                                gap="0px"
                                ml="2">
                            <Text fontSize="xs" fontWeight="bold" lineHeight="1.2">
                                {t('company')}
                            </Text>
                            <Text fontSize="xs" lineHeight="1.2">
                                {selectedCompany ? selectedCompany.name : t('chooseCompany')}
                            </Text>
                        </VStack>
                        <Box display={{base: 'none', md: 'flex'}}>
                            <FiChevronDown/>
                        </Box>
                    </HStack>
                </Button>
            </MenuTrigger>
            <MenuContent bg={themeColors.bgColorSecondary} mt={2} p={1}>
                {companies.length > 0 ? (
                    companies.map((company) => (
                        <MenuItem
                            key={company.name}
                            value={company.name}
                            bg={themeColors.bgColorSecondary}
                            color={themeColors.fontColor}
                            rounded="md"
                            p={2}
                            _hover={{
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor,
                                color: themeColors.fontColorHover
                            }}
                            onClick={() => handleSelectCompany(company)}>
                            {company.name}
                        </MenuItem>
                    ))
                ) : (
                    <MenuItem
                        key={""}
                        value={""}
                        disabled>{t('noCompanies')}</MenuItem>
                )}
            </MenuContent>
        </MenuRoot>
    );
};

export default CompanySelector;
