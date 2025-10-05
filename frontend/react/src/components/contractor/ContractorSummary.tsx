import React from 'react';
import {Box, Separator, Table, Text} from '@chakra-ui/react';
import {ContractorFormValues} from "@/types/contractor-types.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import ContractorBaseSummary from "@/components/contractor/ContractorBaseSummary.tsx";


interface Contact {
    firstName: string;
    lastName: string;
    phoneNumber: string;
}

interface ContractorSummaryProps {
    contractorData?: ContractorFormValues;
    addedContacts?: Contact[];
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
            <ContractorBaseSummary contractorData={contractorData}/>
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
                            {addedContacts && addedContacts!.length > 0 ? (
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
