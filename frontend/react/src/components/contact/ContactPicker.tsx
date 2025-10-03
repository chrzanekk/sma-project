import {FormikProps} from "formik";
import React from "react";
import {ContactBaseDTO, ContactDTO} from "@/types/contact-types.ts";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, Flex, Grid, GridItem, Stack, Text} from "@chakra-ui/react";
import ContactSearchWithSelect from "@/components/contact/ContactSearchWithSelect.tsx";

interface Props {
    formikRef: React.RefObject<FormikProps<any>>;
    selected: ContactBaseDTO | null;
    onSelectChange: (c: ContactBaseDTO | null) => void;
    searchFn: (q: string) => Promise<ContactDTO[]>;
}

const ContactPicker: React.FC<Props> = ({formikRef, selected, onSelectChange, searchFn}) => {
    const {t} = useTranslation(["common", "contacts"]);
    const themeColors = useThemeColors();

    const handleSelect = (c: ContactDTO) => {
        const base: ContactBaseDTO = {
            id: c.id,
            firstName: c.firstName,
            lastName: c.lastName,
            email: c.email,
            phoneNumber: c.phoneNumber,
            additionalInfo: c.additionalInfo,
            company: c.company
        };
        formikRef.current?.setFieldTouched("contact", true, false);
        formikRef.current?.setFieldValue("contact", base, true);
        onSelectChange(base);
    };

    const handleReset = () => {
        formikRef.current?.setFieldTouched("contact", true, false);
        formikRef.current?.setFieldValue("contact", null, true);
        onSelectChange(null);
    }

    return (
        <Box>
            <ContactSearchWithSelect
                searchFn={async (q) => {
                    return await searchFn(q);
                }}
                onSelect={handleSelect}
                minChars={2}
                debounceMs={300}
                size={"md"}
            />
            {selected && (
                <Stack mt={2}>
                    <Box borderWidth={"1px"} borderRadius={"md"} overflow={"hidden"}>
                        <Grid templateColumns="repeat(12,1fr)" gap={0}>
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight={"bold"} mb={1} color={themeColors.fontColor}>
                                    {t("contacts:firstName")}
                                </Text>
                            </GridItem>

                            <GridItem colSpan={9} borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>{selected.firstName}</Text>
                            </GridItem>
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight={"bold"} mb={1} color={themeColors.fontColor}>
                                    {t("contacts:lastName")}
                                </Text>
                            </GridItem>

                            <GridItem colSpan={9} borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>{selected.lastName}</Text>
                            </GridItem>
                            <GridItem colSpan={3} borderRightWidth="1px" borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text fontWeight={"bold"} mb={1} color={themeColors.fontColor}>
                                    {t("contacts:phoneNumber")}
                                </Text>
                            </GridItem>

                            <GridItem colSpan={9} borderBottomWidth="1px"
                                      borderColor="gray.400"
                                      p={2}>
                                <Text color={themeColors.fontColor}>{selected.phoneNumber}</Text>
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

export default ContactPicker;