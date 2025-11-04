import React from "react";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";

interface EmployeeLayoutProps {
    filters: React.ReactNode;
    addEmployeeButton: React.ReactNode;
    table?: React.ReactNode;
    pagination: React.ReactNode;
}

const EmployeeLayout: React.FC<EmployeeLayoutProps> = ({
                                                           filters,
                                                           addEmployeeButton,
                                                           table,
                                                           pagination
                                                       }) => {
    const {t} = useTranslation('employees');
    return (
        <BasicLayout headerTitle={t('employees:employeesList')}
                     filters={filters}
                     addButton={addEmployeeButton}
                     table={table}
                     pagination={pagination}
                     bgColor={useThemeColors().bgColorPrimary}/>
    );
};
export default EmployeeLayout;