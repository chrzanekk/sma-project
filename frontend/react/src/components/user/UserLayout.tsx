import React from "react";
import { Grid, GridItem } from "@chakra-ui/react";

interface UserLayoutProps {
    filters: React.ReactNode; // Komponent filtrów
    addUserButton: React.ReactNode; // Przycisk dodaj usera
    table: React.ReactNode; // Tabela
    pagination: React.ReactNode; // Komponent paginacji
    bgColor?: string; // Opcjonalny kolor tła dla każdego elementu
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
            height="calc(100vh - 140px)"
            gap={2}
            px={4} // Dodanie marginesów z lewej i prawej strony
            py={4} // Dodanie odstępów od góry i dołu
        >
            {/* Wiersz 1: Filtry */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={1}
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
                p={1}
                borderRadius="lg"
                overflow="auto"
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
