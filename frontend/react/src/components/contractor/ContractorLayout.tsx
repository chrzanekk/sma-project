import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ContractorLayoutProps {
    filters: React.ReactNode;
    addContractorButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ContractorLayout: React.FC<ContractorLayoutProps> = ({
                                                               filters,
                                                               addContractorButton,
                                                               table,
                                                               pagination,
                                                               bgColor = useThemeColors().bgColorPrimary
                                                           }) => {
    const {t} = useTranslation('contractors');
    return (
        <BasicLayout headerTitle={t('contractors:contractorList')}
                     filters={filters}
                     addButton={addContractorButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    );
};

export default ContractorLayout;