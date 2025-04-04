import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface UserLayoutProps {
    filters: React.ReactNode;
    addUserButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const UserLayout: React.FC<UserLayoutProps> = ({
                                                   filters,
                                                   addUserButton,
                                                   table,
                                                   pagination,
                                                   bgColor = useThemeColors().bgColorPrimary
                                               }) => {
    const {t} = useTranslation();
    return (
        <BasicLayout headerTitle={t('userList')}
                     filters={filters}
                     addButton={addUserButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>

    );
};

export default UserLayout;
