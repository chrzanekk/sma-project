import React from "react";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ScaffoldingLogPositionLayoutProps {
    filters: React.ReactNode;
    addLogPositionButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor: string;
}

const ScaffoldingLogPositionLayout: React.FC<ScaffoldingLogPositionLayoutProps> = ({
                                                                                       filters,
                                                                                       addLogPositionButton,
                                                                                       table,
                                                                                       pagination,
                                                                                       bgColor
                                                                                   }) => {
    const {t} = useTranslation('scaffoldingLogPositions');
    return (
        <BasicLayout headerTitle={t('scaffoldingLogPositions:positionList')}
                     filters={filters}
                     addButton={addLogPositionButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    );
};

export default ScaffoldingLogPositionLayout;