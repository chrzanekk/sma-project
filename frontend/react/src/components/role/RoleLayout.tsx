import React from "react";
import {themeColors} from "@/theme/theme-colors.ts";
import {Grid, GridItem, Heading} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";

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
                                                   bgColor = themeColors.bgColorPrimary()
                                               }) => {
    const {t} = useTranslation();
    return (
        <Grid
            templateRows="auto auto auto auto 1fr"
            bgColor={themeColors.bgColorSecondary()}
            height="auto"
            gap={1}
        >
            <GridItem
                w="100%"
                p={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} textAlign={"center"} color={themeColors.fontColor()}>
                {t('roleList')}
            </Heading>
            </GridItem>
            {/* Wiersz 1: Filtry */}
            <GridItem
                w="100%"
                p={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} textAlign={"center"} color={themeColors.fontColor()}>
                {t('filters')}
            </Heading>

                {filters}
            </GridItem>

            {/* Wiersz 2: Przycisk Add Role */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={1}
                borderRadius="md"
            >
                {addRoleButton}
            </GridItem>

            {/* Wiersz 3: Tabela */}
            <GridItem
                w="100%"
                borderRadius="lg"
                overflowY="auto"
                p={2}
            >
                {table}
            </GridItem>

            {/* Wiersz 4: Paginacja */}
            <GridItem>
                {pagination}
            </GridItem>
        </Grid>
    );
};

export default RoleLayout