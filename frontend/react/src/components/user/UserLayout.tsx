import React from "react";
import {Grid, GridItem, Heading} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

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
                                                         bgColor = themeColors.bgColor()
                                                     }) => {
    const {t} = useTranslation();
    return (
        <Grid
            templateRows="auto auto auto 1fr auto"
            bgColor={themeColors.bgColorLight()}
            height="calc(100vh - 140px)"
            gap={2}
            px={4}
            py={2}
        >
            <GridItem
                w="100%"
                p={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} textAlign={"center"} color={themeColors.fontColor()}>
                {t('userList')}
            </Heading>
            </GridItem>
            {/* Wiersz 1: Filtry */}
            <GridItem
                w="100%"
                p={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} mb={2} textAlign={"center"} color={themeColors.fontColor()}>
                {t('filters')}
            </Heading>

                {filters}
            </GridItem>

            {/* Wiersz 2: Przycisk Add User */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={1}
                borderRadius="lg"
            >
                {addUserButton}
            </GridItem>

            {/* Wiersz 3: Tabela */}
            <GridItem
                w="100%"
                borderRadius="lg"
                overflow="auto"
                p={1}
            >
                {table}
            </GridItem>

            {/* Wiersz 4: Paginacja */}
            <GridItem
            >
                {pagination}
            </GridItem>
        </Grid>
    );
};

export default UserLayout;
