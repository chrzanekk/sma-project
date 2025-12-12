// components/contract/ContractorPicker.tsx
import React from "react";
import {Box, Button, Flex, Grid, GridItem, Stack, Text} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FormikProps} from "formik";
import {ContractorBaseDTO, ContractorDTO} from "@/types/contractor-types";
import {useThemeColors} from "@/theme/theme-colors.ts";
import ContractorSearchWithSelect from "@/components/contractor/ContractorSearchWithSelect.tsx";

interface Props {
    formikRef: React.RefObject<FormikProps<any> | null>;
    selected: ContractorBaseDTO | null;
    onSelectChange: (c: ContractorBaseDTO | null) => void;
    searchFn: (q: string) => Promise<ContractorDTO[]>;
}

const ContractorPicker: React.FC<Props> = ({formikRef, selected, onSelectChange, searchFn}) => {
    const {t} = useTranslation(["common", "contractors"]);
    const themeColors = useThemeColors();

    const handleSelect = (c: ContractorDTO) => {
        const base: ContractorBaseDTO = {
            id: c.id,
            name: c.name,
            taxNumber: c.taxNumber,
            street: c.street,
            buildingNo: c.buildingNo,
            apartmentNo: c.apartmentNo,
            postalCode: c.postalCode,
            city: c.city,
            country: c.country,
            customer: c.customer,
            supplier: c.supplier,
            scaffoldingUser: c.scaffoldingUser,
            company: c.company,
        };
        formikRef.current?.setFieldTouched("contractor", true, false);
        formikRef.current?.setFieldValue("contractor", base, true);
        onSelectChange(base);
    };

    const handleReset = () => {
        formikRef.current?.setFieldTouched("contractor", true, false);
        formikRef.current?.setFieldValue("contractor", null, true);
        onSelectChange(null);
    };

    return (
        <Box>
            <ContractorSearchWithSelect
                searchFn={async (q) => {
                    return await searchFn(q);
                }}
                onSelect={handleSelect}
                minChars={2}
                debounceMs={300}
                autoSearch={true}
                size="md"
            />
            {selected && (
                <Stack mt={2}>
                    <Box borderWidth="1px" borderRadius="md" overflow="hidden">
                        <Grid templateColumns="repeat(12, 2fr)" gap={0}>
                            {/* Wiersz 1: NAME i TAX NUMBER */}
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                    {t("contractors:name", "NAME")}
                                </Text>
                            </GridItem>

                            <GridItem colSpan={9} borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>{selected.name}</Text>
                            </GridItem>
                            {/* Wiersz 2 NIP */}
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px" borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                    {t("contractors:taxNumber", "Tax Number")}
                                </Text>
                            </GridItem>
                            <GridItem colSpan={9} borderBottomWidth="1px" borderColor="gray.400" p={2}>
                                <Text color={themeColors.fontColor}>{selected.taxNumber}</Text>
                            </GridItem>


                            {/* Wiersz 3: Adres */}
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px" borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                    {t("contractors:address", "Address")}
                                </Text>
                            </GridItem>

                            <GridItem colSpan={9} borderBottomWidth="1px" borderColor="gray.400" p={2}>
                                <Text color={themeColors.fontColor}>
                                    {selected.street} {selected.buildingNo}
                                    {selected.apartmentNo && selected.apartmentNo.trim() !== ""
                                        ? "/" + selected.apartmentNo
                                        : ""}
                                    , {selected.postalCode} {selected.city},{" "}
                                    {typeof selected!.country === "object"
                                        ? (selected!.country as { name: string }).name
                                        : selected!.country}
                                </Text>
                            </GridItem>
                        </Grid>
                    </Box>
                    <Flex justify={"center"}><Button size="2xs" colorPalette="red" onClick={handleReset}>
                        {t("common:resetSelected")}
                    </Button></Flex>
                </Stack>
            )}
        </Box>
    );
};

export default ContractorPicker;
