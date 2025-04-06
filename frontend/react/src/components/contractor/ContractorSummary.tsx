import React from 'react';
import {Box, Grid, GridItem, Separator, Table, Text} from '@chakra-ui/react';
import {ContractorFormValues} from "@/types/contractor-types.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";


interface Contact {
    firstName: string;
    lastName: string;
    phoneNumber: string;
}

interface ContractorSummaryProps {
    contractorData?: ContractorFormValues;
    addedContacts: Contact[];
    deletedContacts?: Contact[] | [];
}

const ContractorSummary: React.FC<ContractorSummaryProps> = ({contractorData, addedContacts, deletedContacts}) => {
    const themeColors = useThemeColors();
    const {t} = useTranslation(["common", "contractors", "errors", "contacts"]);

    return (
        <Box mt={2}>
            <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                {t("contractors:details")}
            </Text>

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

            <Separator/>

            <Box mt={2}>
                <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                    {t("contacts:contactAddedList", "Lista dodanych kontaktów")}
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
                                    {t("contacts:firstName")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                    {t("contacts:lastName")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                    {t("contacts:phoneNumber")}
                                </Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>

                        <Table.Body>
                            {addedContacts.length > 0 ? (
                                addedContacts.map((contact, idx) => (
                                    <Table.Row
                                        key={idx}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover
                                        }}
                                    >
                                        <Table.Cell textAlign="center">{contact.firstName}</Table.Cell>
                                        <Table.Cell textAlign="center">{contact.lastName}</Table.Cell>
                                        <Table.Cell textAlign="center">{contact.phoneNumber}</Table.Cell>
                                    </Table.Row>
                                ))
                            ) : (
                                <Box textAlign="center" py={2}>
                                    {t("contacts:noContacts", "Brak kontaktów")}
                                </Box>
                            )}
                        </Table.Body>
                    </Table.Root>
                </Table.ScrollArea>
            </Box>
            <Box mt={2}>
                <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                    {t("contacts:contactDeletedList", "Lista usuniętch kontaktów")}
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
                                    {t("contacts:firstName")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                    {t("contacts:lastName")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                    {t("contacts:phoneNumber")}
                                </Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>

                        <Table.Body>
                            {deletedContacts && deletedContacts!.length > 0 ? (
                                deletedContacts!.map((contact, idx) => (
                                    <Table.Row
                                        key={idx}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover
                                        }}
                                    >
                                        <Table.Cell textAlign="center">{contact.firstName}</Table.Cell>
                                        <Table.Cell textAlign="center">{contact.lastName}</Table.Cell>
                                        <Table.Cell textAlign="center">{contact.phoneNumber}</Table.Cell>
                                    </Table.Row>
                                ))
                            ) : (
                                <Box textAlign="center" py={2}>
                                    {t("contacts:noContacts", "Brak kontaktów")}
                                </Box>
                            )}
                        </Table.Body>
                    </Table.Root>
                </Table.ScrollArea>
            </Box>
        </Box>
    );
};

export default ContractorSummary;
