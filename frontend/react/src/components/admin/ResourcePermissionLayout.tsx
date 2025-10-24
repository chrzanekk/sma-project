import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ResourcePermissionLayoutProps {
    table: React.ReactNode;
    bgColor?: string;
    headerTitle: string;
}

const ResourcePermissionLayout: React.FC<ResourcePermissionLayoutProps> = ({
                                                                               table,
                                                                               bgColor,
                                                                               headerTitle
                                                                           }) => {
    const themeColors = useThemeColors();
    const layoutBgColor = bgColor || themeColors.bgColorPrimary;
    return (
        <BasicLayout
            headerTitle={headerTitle}
            table={table}
            bgColor={layoutBgColor}/>
    )
}

export default ResourcePermissionLayout;