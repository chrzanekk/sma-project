import React from "react";
import { Grid, GridItem, Heading } from "@chakra-ui/react";
import { useThemeColors } from "@/theme/theme-colors";
import { useTranslation } from "react-i18next";

export interface BasicLayoutProps {
    headerTitle: React.ReactNode;
    filtersTitle?: React.ReactNode;
    filters: React.ReactNode;
    addButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const BasicLayout: React.FC<BasicLayoutProps> = ({
                                                     headerTitle,
                                                     filters,
                                                     addButton,
                                                     table,
                                                     pagination,
                                                     bgColor,
                                                 }) => {
    const themeColors = useThemeColors();
    const { t } = useTranslation(["common"]);
    const layoutBgColor = bgColor || themeColors.bgColorPrimary;

    return (
        <Grid
            templateRows="auto auto auto auto 1fr"
            bgColor={themeColors.bgColorSecondary}
            height="auto"
            gap={1}
        >
            <GridItem w="100%" p={1} mt={1} bg={layoutBgColor} borderRadius="lg">
                <Heading size="sm" fontSize={14} textAlign="center" color={themeColors.fontColor}>
                    {headerTitle}
                </Heading>
            </GridItem>
            <GridItem w="100%" p={1} bg={layoutBgColor} borderRadius="lg">
                <Heading size="sm" fontSize={14} textAlign="center" color={themeColors.fontColor}>
                    {t("filters")}
                </Heading>
                {filters}
            </GridItem>
            <GridItem w="100%" bg={layoutBgColor} p={1} borderRadius="lg">
                {addButton}
            </GridItem>
            <GridItem w="100%" borderRadius="lg" overflowY="auto" p={2}>
                {table}
            </GridItem>
            <GridItem>{pagination}</GridItem>
        </Grid>
    );
};

export default BasicLayout;
