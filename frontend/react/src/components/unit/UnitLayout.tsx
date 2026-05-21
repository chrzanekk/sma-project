import React from "react";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";

interface UnitLayoutProps {
    filters: React.ReactNode;
    addUnitButton: React.ReactNode;
    table?: React.ReactNode;
    pagination: React.ReactNode;
}


const UnitLayout: React.FC<UnitLayoutProps> = ({
                                                   filters,
                                                   addUnitButton,
                                                   table,
                                                   pagination
                                               }) => {
    const {t} = useTranslation('units');
    return (
        <BasicLayout headerTitle={t('units:unitList')}
                     filters={filters}
                     addButton={addUnitButton}
                     table={table}
                     pagination={pagination}
                     bgColor={useThemeColors().bgColorPrimary}/>
    );
};
export default UnitLayout;