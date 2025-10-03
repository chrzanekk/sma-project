// components/contract/ConstructionSitePicker.tsx
import React from "react";
import {Box, Button, Flex, Grid, GridItem, Stack, Text} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FormikProps} from "formik";
import {ConstructionSiteBaseDTO, ConstructionSiteDTO,} from "@/types/constrution-site-types";
import {useThemeColors} from "@/theme/theme-colors.ts";
import ConstructionSiteSearchWithSelect from "@/components/constructionsite/ConstructionSiteSearchWithSelect.tsx";

interface Props {
    formikRef: React.RefObject<FormikProps<any>>;
    selected: ConstructionSiteBaseDTO | null;
    onSelectChange: (c: ConstructionSiteBaseDTO | null) => void;
    searchFn: (q: string) => Promise<ConstructionSiteDTO[]>; // zwraca pełne, wybieramy base
}

const ConstructionSitePicker: React.FC<Props> = ({formikRef, selected, onSelectChange, searchFn}) => {
    const {t} = useTranslation(["common", "constructionSites"]);
    const themeColors = useThemeColors();

    const handleSelect = (s: ConstructionSiteDTO) => {
        const base: ConstructionSiteBaseDTO = {
            id: s.id,
            name: s.name,
            address: s.address,
            country: s.country,
            shortName: s.shortName,
            code: s.code,
        };
        formikRef.current?.setFieldTouched("constructionSite", true, false);
        formikRef.current?.setFieldValue("constructionSite", base, true);
        onSelectChange(base);
    };

    const handleReset = () => {
        formikRef.current?.setFieldTouched("constructionSite", true, false);
        formikRef.current?.setFieldValue("constructionSite", null, true);
        onSelectChange(null);
    };

    return (
        <Box>
            <ConstructionSiteSearchWithSelect
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
                        <Grid templateColumns={"repeat(12,1fr)"} gap={0}>
                            {/* Wiersz 1 nazwa budowy*/}
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                    {t("constructionSites:name")}
                                </Text>
                            </GridItem>
                            <GridItem colSpan={9} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>
                                    {selected.name}
                                </Text>
                            </GridItem>

                             {/* Wiersz 2 adres*/}
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                    {t("constructionSites:address")}
                                </Text>
                            </GridItem>
                            <GridItem colSpan={9} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>
                                    {selected.address}
                                </Text>
                            </GridItem>

                             {/* Wiersz 2 Kraj*/}
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight="bold" mb={1} color={themeColors.fontColor}>
                                    {t("constructionSites:country")}
                                </Text>
                            </GridItem>
                            <GridItem colSpan={9} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>
                                    {selected.country.name}
                                </Text>
                            </GridItem>


                        </Grid>
                    </Box>
                    <Flex justify={"center"}>
                        <Button size="2xs" colorPalette="red" onClick={handleReset}>
                            {t("common:resetSelected")}
                        </Button>
                    </Flex>
                </Stack>
            )}
        </Box>
    );
};

export default ConstructionSitePicker;
