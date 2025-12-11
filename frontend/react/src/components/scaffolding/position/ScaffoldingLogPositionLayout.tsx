import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ScaffoldingLogPositionLayoutProps {
    filters: React.ReactNode;
    addContactButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ScaffoldingLogPositionLayout: React.FC<ScaffoldingLogPositionLayoutProps> = ({
                                                         filters,
                                                         addContactButton,
                                                         table,
                                                         pagination,
                                                         bgColor = useThemeColors().bgColorPrimary
                                                     }) => {
    const {t} = useTranslation('scaffoldingLogPositions');
    return (
        <BasicLayout headerTitle={t('scaffoldingLogPositions:positionList')}
                     filters={filters}
                     addButton={addContactButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    );
};

export default ScaffoldingLogPositionLayout;