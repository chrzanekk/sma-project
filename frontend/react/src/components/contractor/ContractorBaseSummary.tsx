import {ContractorFormValues} from "@/types/contractor-types.ts";
import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {Box, Grid, GridItem, Text} from "@chakra-ui/react";

interface ContractorBaseSummaryProps {
    contractorData?: ContractorFormValues;
}


const ContractorBaseSummary: React.FC<ContractorBaseSummaryProps> = ({contractorData}) => {
    const themeColors = useThemeColors();
    const {t} = useTranslation(["common", "contractors", "errors"]);

    return (
        <Box mt={2}>
            {contractorData && (
                <Box borderWidth="1px" borderRadius="md" overflow="hidden">
                    <Grid templateColumns="repeat(6, 1fr)" gap={0}>
                        {/* Wiersz 1: NAME i TAX NUMBER */}
                        <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px" borderColor="gray.400"
                                  p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("contractors:name", "NAME")}
                            </Text>
                            <Text color={themeColors.fontColor}>{contractorData.name}</Text>
                        </GridItem>
                        <GridItem colSpan={3} borderBottomWidth="1px" borderColor="gray.400" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("contractors:taxNumber", "Tax Number")}
                            </Text>
                            <Text color={themeColors.fontColor}>{contractorData.taxNumber}</Text>
                        </GridItem>

                        {/* Wiersz 2: Adres */}
                        <GridItem colSpan={6} borderBottomWidth="1px" borderColor="gray.400" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("contractors:address", "Address")}
                            </Text>
                            <Text color={themeColors.fontColor}>
                                {contractorData.street} {contractorData.buildingNo}
                                {contractorData.apartmentNo && contractorData.apartmentNo.trim() !== ""
                                    ? "/" + contractorData.apartmentNo
                                    : ""}
                                , {contractorData.postalCode} {contractorData.city},{" "}
                                {typeof contractorData!.country === "object"
                                    ? (contractorData!.country as { name: string }).name
                                    : contractorData!.country}
                            </Text>
                        </GridItem>

                        {/* Wiersz 3: Boolean values – Customer, Supplier, ScaffoldingUser */}
                        <GridItem colSpan={2} borderRightWidth="1px" borderColor="gray.400" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("contractors:customer", "Customer")}
                            </Text>
                            <Text color={themeColors.fontColor}>
                                {contractorData.customer ? t("common:yes", "Yes") : t("common:no", "No")}
                            </Text>
                        </GridItem>
                        <GridItem colSpan={2} borderRightWidth="1px" borderColor="gray.400" p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("contractors:supplier", "Supplier")}
                            </Text>
                            <Text color={themeColors.fontColor}>
                                {contractorData.supplier ? t("common:yes", "Yes") : t("common:no", "No")}
                            </Text>
                        </GridItem>
                        <GridItem colSpan={2} p={2}>
                            <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                {t("contractors:scaffoldingUser", "Scaffolding User")}
                            </Text>
                            <Text color={themeColors.fontColor}>
                                {contractorData.scaffoldingUser ? t("common:yes", "Yes") : t("common:no", "No")}
                            </Text>
                        </GridItem>
                    </Grid>
                </Box>
            )}
        </Box>
    );
};

export default ContractorBaseSummary;
