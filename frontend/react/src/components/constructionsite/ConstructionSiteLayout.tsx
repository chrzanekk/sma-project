import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ConstructionSiteProps {
    filters: React.ReactNode;
    addConstructionSiteButton: React.ReactNode;
    content: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ConstructionSiteLayout: React.FC<ConstructionSiteProps> = ({
                                                                     filters,
                                                                     addConstructionSiteButton,
                                                                     content,
                                                                     pagination,
                                                                     bgColor = useThemeColors().bgColorPrimary
                                                                 }) => {
    const {t} = useTranslation('constructionSites');
    return (
        <BasicLayout headerTitle={t('constructionSites:list')}
                     filters={filters}
                     addButton={addConstructionSiteButton}
                     table={content}
                     pagination={pagination}
                     bgColor={bgColor}/>
    )
}
export default ConstructionSiteLayout;