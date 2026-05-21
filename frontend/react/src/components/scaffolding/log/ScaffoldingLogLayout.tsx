import React from "react";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";

interface ScaffoldingLogLayoutProps {
    filters: React.ReactNode;
    addLogButton: React.ReactNode;
    table?: React.ReactNode;
    pagination: React.ReactNode;
}

const ScaffoldingLogLayout: React.FC<ScaffoldingLogLayoutProps> = ({
                                                                       filters,
                                                                       addLogButton,
                                                                       table,
                                                                       pagination
                                                                   }) => {
    const {t} = useTranslation('scaffoldingLogs');
    return (
        <BasicLayout headerTitle={t('scaffoldingLogs:logList')}
                     filters={filters}
                     addButton={addLogButton}
                     table={table}
                     pagination={pagination}
                     bgColor={useThemeColors().bgColorPrimary}/>
    )
}
export default ScaffoldingLogLayout;