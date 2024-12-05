import React from "react";
import { Grid, GridItem } from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";

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
                                                         bgColor = "green.300", // Domyślny kolor tła dla każdego elementu
                                                     }) => {
    return (
        <Grid
            templateRows="auto auto 1fr auto"
            bgColor={themeColors.bgColorLight()}
            height="calc(100vh - 140px)"
            gap={2}
            px={4} // Dodanie marginesów z lewej i prawej strony
            py={2} // Dodanie odstępów od góry i dołu
        >
            {/* Wiersz 1: Filtry */}
            <GridItem
                w="100%"
                p={1}
                bg={bgColor}
                borderRadius="lg"
            >
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
