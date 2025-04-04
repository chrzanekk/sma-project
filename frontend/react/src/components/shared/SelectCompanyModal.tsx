import React, {useState} from "react";
import {Button, Dialog, Flex, Heading, Portal, Text, VStack} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {CompanyBaseDTO} from "@/types/company-type";
import {CustomSimpleSelect} from "@/components/shared/CustomFormFields";
import {useTranslation} from "react-i18next";
import {useAuth} from "@/context/AuthContext";


interface SelectCompanyModalProps {
    isOpen: boolean;
    companies: CompanyBaseDTO[];
    onClose: () => void;
    onConfirm: (company: CompanyBaseDTO | null) => void;
}

const SelectCompanyModal: React.FC<SelectCompanyModalProps> = ({
                                                                   isOpen,
                                                                   companies,
                                                                   onClose,
                                                                   onConfirm
                                                               }) => {
    const themeColors = useThemeColors();
    const [selectedCompanyId, setSelectedCompanyId] = useState<number | null>(null);
    const {t} = useTranslation(['common', 'navbar', 'companies']);
    const {logOut} = useAuth();

    const handleConfirm = () => {
        const company = companies.find(c => c?.id === selectedCompanyId) || null;
        onConfirm(company);
    };

    const companyOptions = companies.map(company => ({
        value: company.id!,
        label: company.name
    }));

    return (
        <Dialog.Root
            open={isOpen}
            onOpenChange={(open) => !open && onClose()}
            role="alertdialog"
            size="full" motionPreset="slide-in-bottom"
        >
            <Portal>
                <Dialog.Backdrop/>
                <Dialog.Positioner>
                    <Dialog.Content backgroundColor={themeColors.bgColorSecondary}>
                        {/*<Dialog.Header>*/}
                        {/*    <Dialog.Title color={themeColors.fontColor}>{t('navbar:chooseCompany')}</Dialog.Title>*/}
                        {/*</Dialog.Header>*/}
                        <Dialog.Body>
                            {companies.length > 0 ? (
                                <Flex justifyContent={'center'} mt={10}>
                                    <VStack gap={4}>
                                        <Heading color={themeColors.fontColor} mb={5}
                                                 size={'3xl'}>{t('navbar:chooseCompany')}</Heading>
                                        <CustomSimpleSelect
                                            placeholder={t('companies:name')}
                                            value={selectedCompanyId ?? -1}
                                            onChange={(val) => setSelectedCompanyId(val)}
                                            options={companyOptions}
                                            width="100%"
                                            bgColor={themeColors.bgColorPrimary}
                                            size="sm"
                                        />
                                        <Flex justifyContent={'center'} mt={5}>
                                            <Button onClick={handleConfirm} colorPalette="blue" size={"xl"}>
                                                {t('common:choose')}
                                            </Button>
                                        </Flex>
                                    </VStack>
                                </Flex>
                            ) : (
                                <Flex justifyContent="center" alignItems="center" h="60vh">
                                    <VStack gap={5}>
                                        <Heading size="lg" color={themeColors.fontColor}>
                                            {t('navbar:noCompanies')}
                                        </Heading>
                                        <Text color={themeColors.fontColor} maxW="500px" textAlign="center">
                                            {t('companies:noCompaniesInfo')}
                                        </Text>
                                        <Button onClick={() => onConfirm(null)} colorPalette="blue" size="lg">
                                            {t('common:continue')}
                                        </Button>
                                        <Text color={themeColors.fontColor} fontSize="sm" opacity={0.6}>
                                            {t('common:or')}
                                        </Text>
                                        <Button onClick={() => logOut()} colorPalette="teal" size="sm">
                                            {t('common:logout')}
                                        </Button>
                                    </VStack>
                                </Flex>
                            )}
                        </Dialog.Body>
                        <Dialog.Footer>
                        </Dialog.Footer>
                    </Dialog.Content>
                </Dialog.Positioner>
            </Portal>
        </Dialog.Root>
    );
};

export default SelectCompanyModal;
