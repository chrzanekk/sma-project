import {FormikProps} from "formik";
import React, {useMemo} from "react";
import {ContactBaseDTO, ContactDTO} from "@/types/contact-types.ts";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Grid, GridItem, Stack, Text} from "@chakra-ui/react";
import ContactSearchWithSelect from "@/components/contact/ContactSearchWithSelect.tsx";
import {makeContactSearchAdapter} from "@/search/contact-search-adapter.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";

interface Props {
    formikRef: React.RefObject<FormikProps<any> | null>;
    selected: ContactBaseDTO | undefined;
    onSelectChange: (c: ContactBaseDTO | null) => void;
    contractorId?: number;
    showDetails?: boolean;
    placeholder?: string;
    fieldName?: string;
    label?: string
}

const ContactPicker: React.FC<Props> = ({
                                            formikRef,
                                            selected,
                                            onSelectChange,
                                            contractorId,
                                            showDetails = true,
                                            placeholder,
                                            fieldName = "contact",
                                            label
                                        }) => {
    const {t} = useTranslation(["common", "contacts"]);
    const themeColors = useThemeColors();
    const companyId = getSelectedCompanyId()!;

    const searchFn = useMemo(() => {
        return makeContactSearchAdapter({
            fixed: {companyId, contractorId},
            defaults: {page: 0, size: 10, sort: "id,asc"},
        });
    }, [companyId, contractorId]);

    const handleSelect = (c: ContactDTO | null) => {
        if (!c) {
            formikRef.current?.setFieldTouched(fieldName, true, false);
            formikRef.current?.setFieldValue(fieldName, null, true);
            onSelectChange(null);
            return;
        }

        const base: ContactBaseDTO = {
            id: c.id,
            firstName: c.firstName,
            lastName: c.lastName,
            email: c.email,
            phoneNumber: c.phoneNumber,
            additionalInfo: c.additionalInfo,
            company: c.company
        };
        formikRef.current?.setFieldTouched(fieldName, true, false);
        formikRef.current?.setFieldValue(fieldName, base, true);
        onSelectChange(base);
    };

    const searchPlaceholder = placeholder ?? t("contacts:searchByLastName");

    return (
        <Box>
            <ContactSearchWithSelect
                searchFn={searchFn}
                onSelect={handleSelect}
                minChars={2}
                debounceMs={300}
                size={"md"}
                placeholder={searchPlaceholder}
                label={label}
            />
            {selected && (
                <Stack mt={2}>
                    {showDetails && (
                        <>
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
                        </>
                    )}
                </Stack>
            )}
        </Box>
    );
};

export default ContactPicker;