import React from "react";
import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {Grid, GridItem, Heading} from "@chakra-ui/react";

interface ContractorLayoutProps {
    filters: React.ReactNode;
    addContractorButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ContractorLayout: React.FC<ContractorLayoutProps> = ({
                                                               filters,
                                                               addContractorButton,
                                                               table,
                                                               pagination,
                                                               bgColor = themeColors.bgColorPrimary()
                                                           }) => {
    const {t} = useTranslation(['common','contractors']);
    return (
        <Grid
            templateRows="auto auto auto auto 1fr"
            bgColor={themeColors.bgColorSecondary()}
            height="100vh" // Pełna wysokość ekranu
            gap={1}
        >
            <GridItem
                w="100%"
                p={1}
                mt={1}
                bg={bgColor}
                borderRadius="lg"
            ><Heading size={"sm"} fontSize={14} textAlign={"center"} color={themeColors.fontColor()}>
                {t('contractors:contractorList')}
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

            {/* Wiersz 2: Przycisk Add User */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={1}
                borderRadius="lg"
            >
                {addContractorButton}
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

export default ContractorLayout;