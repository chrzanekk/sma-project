import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {Grid, GridItem, Heading} from "@chakra-ui/react";

interface ContactLayoutProps {
    filters: React.ReactNode;
    addContactButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ContactLayout: React.FC<ContactLayoutProps> = ({
                                                         filters,
                                                         addContactButton,
                                                         table,
                                                         pagination,
                                                         bgColor = useThemeColors().bgColorPrimary
                                                     }) => {
    const {t} = useTranslation(['common', 'contacts']);
    const themeColors = useThemeColors();
    return (
        <Grid
            templateRows="auto auto auto auto 1fr"
            bgColor={themeColors.bgColorSecondary}
            height="auto"
            gap={1}
        >
            <GridItem
                w="100%"
                p={1}
                mt={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} textAlign={"center"} color={themeColors.fontColor}>
                {t('contacts:contactList')}
            </Heading>
            </GridItem>
            {/* Wiersz 1: Filtry */}
            <GridItem
                w="100%"
                p={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} textAlign={"center"} color={themeColors.fontColor}>
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
                {addContactButton}
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
            <GridItem
            >
                {pagination}
            </GridItem>
        </Grid>
    );
};

export default ContactLayout;