import React from "react";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface CompanyLayoutProps {
    filters: React.ReactNode;
    addCompanyButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const CompanyLayout: React.FC<CompanyLayoutProps> = ({filters, addCompanyButton, table, pagination, bgColor}) => {
    const {t} = useTranslation('companies');
    return (
        <BasicLayout headerTitle={t('companiesList')}
                     filters={filters}
                     addButton={addCompanyButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    )
}

export default CompanyLayout;