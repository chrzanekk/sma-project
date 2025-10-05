import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ContractLayoutProps {
    filters: React.ReactNode;
    addContractButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ContactLayout: React.FC<ContractLayoutProps> = ({
                                                         filters,
                                                         addContractButton,
                                                         table,
                                                         pagination,
                                                         bgColor = useThemeColors().bgColorPrimary
                                                     }) => {
    const {t} = useTranslation('contracts');
    return (
        <BasicLayout headerTitle={t('contracts:contractList')}
                     filters={filters}
                     addButton={addContractButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    );
};

export default ContactLayout;