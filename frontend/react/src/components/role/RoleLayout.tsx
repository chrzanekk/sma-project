import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface RoleLayoutProps {
    filters: React.ReactNode;
    addRoleButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const RoleLayout: React.FC<RoleLayoutProps> = ({
                                                   filters,
                                                   addRoleButton,
                                                   table,
                                                   pagination,
                                                   bgColor = useThemeColors().bgColorPrimary
                                               }) => {
    const {t} = useTranslation();

    return (
        <BasicLayout headerTitle={t('roleList')}
                     filters={filters}
                     addButton={addRoleButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>

    );
};

export default RoleLayout