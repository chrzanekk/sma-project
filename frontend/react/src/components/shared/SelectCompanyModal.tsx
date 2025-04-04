import React, {useState} from "react";
import {Button, Dialog, Portal, Text, Box} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {CompanyBaseDTO} from "@/types/company-type";
import {CustomSimpleSelect} from "@/components/shared/CustomFormFields";
import {useTranslation} from "react-i18next";

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
    const {t} = useTranslation(['common','navbar']);

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
        >
            <Portal>
                <Dialog.Backdrop/>
                <Dialog.Positioner>
                    <Dialog.Content backgroundColor={themeColors.bgColorSecondary}>
                        <Dialog.Header>
                            <Dialog.Title>{t('navbar:chooseCompany')}</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body>
                            {companies.length > 0 ? (
                                <Box mb={4}>
                                    <CustomSimpleSelect
                                        value={selectedCompanyId ?? -1}
                                        onChange={(val) => setSelectedCompanyId(val)}
                                        options={companyOptions}
                                        width="100%"
                                        bgColor={themeColors.bgColorPrimary}
                                        size="sm"
                                    />
                                </Box>
                            ) : (
                                <Text color={themeColors.fontColor}>
                                    {t('navbar:noCompanies')}
                                </Text>
                            )}
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Button onClick={handleConfirm} colorScheme="blue">
                                {t('common:choose')}
                            </Button>
                        </Dialog.Footer>
                    </Dialog.Content>
                </Dialog.Positioner>
            </Portal>
        </Dialog.Root>
    );
};

export default SelectCompanyModal;
