import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface PositionLayoutProps {
    filters: React.ReactNode;
    addPositionButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
}

const PositionLayout: React.FC<PositionLayoutProps> = ({
                                                           filters,
                                                           addPositionButton,
                                                           table,
                                                           pagination
                                                       }) => {
    const {t} = useTranslation('positions');
    return (
        <BasicLayout headerTitle={t('positions:positionList')}
                     filters={filters}
                     addButton={addPositionButton}
                     table={table} pagination={pagination}
                     bgColor={useThemeColors().bgColorPrimary}/>
    );
};
export default PositionLayout;