import React from "react";
import {Box, Grid, GridItem, Separator, Table, Text} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {ConstructionSiteFormValues} from "@/types/constrution-site-types.ts";
import {BaseContractorDTO} from "@/types/contractor-types.ts";

interface ConstructionSiteSummaryProps {
    siteData?: ConstructionSiteFormValues;
    addedContractors: BaseContractorDTO[];
    deletedContractors?: BaseContractorDTO[];
}

const ConstructionSiteSummary: React.FC<ConstructionSiteSummaryProps> = ({
                                                                             siteData,
                                                                             addedContractors,
                                                                             deletedContractors = [],
                                                                         }) => {
    const themeColors = useThemeColors();
    const {t} = useTranslation(["common", "constructionSites", "contractors"]);

    return (
        <Box mt={2}>
            <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                {t("constructionSites:details")}
            </Text>

            {siteData && (
                <Box borderWidth="1px" borderRadius="md" overflow="hidden" mb={4}>
                    <Grid templateColumns="repeat(6, 1fr)" gap={0}>
                        {/* Wiersz 1: Name + Short Name */}
                        <GridItem colSpan={4} borderRightWidth="1px" borderBottomWidth="1px" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("constructionSites:name")}
                            </Text>
                            <Text color={themeColors.fontColor}>{siteData.name}</Text>
                        </GridItem>
                        <GridItem colSpan={2} borderBottomWidth="1px" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("constructionSites:shortName")}
                            </Text>
                            <Text color={themeColors.fontColor}>{siteData.shortName}</Text>
                        </GridItem>

                        {/* Wiersz 2: Code + Country */}
                        <GridItem colSpan={2} borderRightWidth="1px" borderBottomWidth="1px" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("constructionSites:code")}
                            </Text>
                            <Text color={themeColors.fontColor}>{siteData.code}</Text>
                        </GridItem>
                        <GridItem colSpan={4} borderBottomWidth="1px" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("constructionSites:country")}
                            </Text>
                            <Text color={themeColors.fontColor}>{siteData.country}</Text>
                        </GridItem>

                        {/* Wiersz 3: Address */}
                        <GridItem colSpan={6} p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("constructionSites:address")}
                            </Text>
                            <Text color={themeColors.fontColor}>{siteData.address}</Text>
                        </GridItem>
                    </Grid>
                </Box>
            )}

            <Separator/>

            {/* Lista dodanych kontrahentów */}
            <Box mt={2}>
                <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                    {t("contractors:addedList")}
                </Text>

                <Table.ScrollArea borderWidth="1px" rounded="sm" height="150px">
                    <Table.Root
                        size="sm"
                        stickyHeader
                        showColumnBorder
                        interactive
                        color={themeColors.fontColor}
                    >
                        <Table.Header>
                            <Table.Row bg={themeColors.bgColorPrimary}>
                                <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                    {t("contractors:name")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                    {t("contractors:taxNumber")}
                                </Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {addedContractors.length > 0 ? (
                                addedContractors.map((contractor, idx) => (
                                    <Table.Row
                                        key={idx}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover
                                        }}
                                    >
                                        <Table.Cell textAlign="center">{contractor.name}</Table.Cell>
                                        <Table.Cell textAlign="center">{contractor.taxNumber}</Table.Cell>
                                    </Table.Row>
                                ))
                            ) : (
                                <Box textAlign="center" py={2}>
                                    {t("contractors:noContractors", "Brak kontrahentów")}
                                </Box>
                            )}
                        </Table.Body>
                    </Table.Root>
                </Table.ScrollArea>
            </Box>

            {/* Lista usuniętych kontrahentów */}
            {deletedContractors.length > 0 && (
                <Box mt={4}>
                    <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                        {t("contractors:deletedList")}
                    </Text>

                    <Table.ScrollArea borderWidth="1px" rounded="sm" height="150px">
                        <Table.Root
                            size="sm"
                            stickyHeader
                            showColumnBorder
                            interactive
                            color={themeColors.fontColor}
                        >
                            <Table.Header>
                                <Table.Row bg={themeColors.bgColorPrimary}>
                                    <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                        {t("contractors:name")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                        {t("contractors:taxNumber")}
                                    </Table.ColumnHeader>
                                </Table.Row>
                            </Table.Header>
                            <Table.Body>
                                {deletedContractors.map((contractor, idx) => (
                                    <Table.Row
                                        key={idx}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover
                                        }}
                                    >
                                        <Table.Cell textAlign="center">{contractor.name}</Table.Cell>
                                        <Table.Cell textAlign="center">{contractor.taxNumber}</Table.Cell>
                                    </Table.Row>
                                ))}
                            </Table.Body>
                        </Table.Root>
                    </Table.ScrollArea>
                </Box>
            )}
        </Box>
    );
};

export default ConstructionSiteSummary;
