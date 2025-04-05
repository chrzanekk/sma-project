// ContractorTableWithContacts.tsx
import React, {useState} from "react";
import {Box, Button, Collapsible, HStack, Spinner, Table, Text,} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import DateFormatter from "@/utils/date-formatter";
import EditContractorDialog from "@/components/contractor/EditContractorDialog";
import {FetchableContractorDTO} from "@/types/contractor-types";
import GenericContactTable from "@/components/contact/GenericContactTable.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {getContactsByContractorIdPaged} from "@/services/contractor-service.ts";
import {ContactDTO} from "@/types/contact-types.ts";


interface ContractorTableWithContactsProps {
    contractors: FetchableContractorDTO[];
    onDelete: (id: number) => void;
    fetchContractors: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    // Funkcje zarządzania kontaktami przekazywane z komponentu nadrzędnego
    contactFetchContacts: (customFilter?: Record<string, any>, page?: number, size?: number) => Promise<void>;
    contactOnDelete: (id: number) => void;
    contactOnSortChange: (field: string) => void;
    contactSortField: string | null;
    contactSortDirection: "asc" | "desc";
}

type ContactState = {
    contacts: ContactDTO[];
    page: number;
    hasMore: boolean;
    loading: boolean;
};

const ContractorTableWithContacts: React.FC<ContractorTableWithContactsProps> = ({
                                                                                     contractors,
                                                                                     onDelete,
                                                                                     fetchContractors,
                                                                                     onSortChange,
                                                                                     sortField,
                                                                                     sortDirection,
                                                                                     contactFetchContacts,
                                                                                     contactOnDelete,
                                                                                     contactOnSortChange,
                                                                                     contactSortField,
                                                                                     contactSortDirection,
                                                                                 }) => {
    const {t} = useTranslation(["common", "contractors"]);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [expandedRows, setExpandedRows] = useState<{ [key: number]: boolean }>({});
    const [contactStates, setContactStates] = useState<{ [key: number]: ContactState }>({});

    const toggleExpand = async (contractorId: number) => {
        setExpandedRows((prev) => ({...prev, [contractorId]: !prev[contractorId]}));

        if (!contactStates[contractorId]) {
            setContactStates((prev) => ({
                ...prev,
                [contractorId]: {
                    contacts: [],
                    page: 0,
                    hasMore: true,
                    loading: true,
                },
            }));

            try {
                const initialContacts = await getContactsByContractorIdPaged(contractorId, 0, 10);
                setContactStates((prev) => ({
                    ...prev,
                    [contractorId]: {
                        contacts: initialContacts.items,
                        page: 1,
                        hasMore: initialContacts.totalPages > 1,
                        loading: false,
                    },
                }));
            } catch (error) {
                console.error("Błąd ładowania kontaktów:", error);
            }
        }
    };

    const loadMoreContacts = async (contractorId: number) => {
        const state = contactStates[contractorId];
        if (!state || !state.hasMore || state.loading) return;

        setContactStates((prev) => ({
            ...prev,
            [contractorId]: {...state, loading: true},
        }));

        try {
            const more = await getContactsByContractorIdPaged(contractorId, state.page, 10);
            setContactStates((prev) => ({
                ...prev,
                [contractorId]: {
                    contacts: [...state.contacts, ...more.items],
                    page: state.page + 1,
                    hasMore: state.page + 1 < more.totalPages,
                    loading: false,
                },
            }));
        } catch (error) {
            console.error("Błąd ładowania kontaktów:", error);
        }
    };

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!contractors || contractors.length === 0) {
        return (
            <Box>
                <Text fontSize={20}>{t("dataNotFound")}</Text>
            </Box>
        );
    }

    return (
        <Box>
            <Table.ScrollArea height={"auto"} borderWidth={"1px"} borderRadius={"md"} borderColor={"grey"}>
                <Table.Root size={"sm"}
                            interactive
                            showColumnBorder
                            color={themeColors.fontColor}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("id")}>
                                ID {renderSortIndicator("id")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("name")}>
                                {t("contractors:name")} {renderSortIndicator("name")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("taxNumber")}>
                                {t("contractors:taxNumber")} {renderSortIndicator("taxNumber")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("street")}>
                                {t("contractors:address")} {renderSortIndicator("street")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("customer")}>
                                {t("contractors:customer")} {renderSortIndicator("customer")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("supplier")}>
                                {t("contractors:supplier")} {renderSortIndicator("supplier")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("scaffoldingUser")}>
                                {t("contractors:scaffoldingUser")} {renderSortIndicator("scaffoldingUser")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("createdDatetime")}>
                                {t("createDate")} {renderSortIndicator("createdDatetime")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("createdBy")}>
                                {t("createdBy")} {renderSortIndicator("createdBy")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("lastModifiedDatetime")}>
                                {t("lastModifiedDate")} {renderSortIndicator("lastModifiedDatetime")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps} onClick={() => onSortChange("modifiedBy")}>
                                {t("lastModifiedBy")} {renderSortIndicator("modifiedBy")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("edit")}</Table.ColumnHeader>
                        </Table.Row>
                    </Table.Header>

                    <Table.Body>
                        {contractors.map((contractor) => {
                            // Używamy asercji, że id jest zdefiniowane
                            const contractorId = contractor.id!;
                            const contactState = contactStates[contractorId];
                            return (
                                <React.Fragment key={contractorId}>
                                    <Table.Row
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover
                                        }}
                                        onClick={() => toggleExpand(contractorId)} style={{cursor: "pointer"}}
                                    >
                                        <Table.Cell {...commonCellProps} width={"2%"}>{contractorId}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"15%"}>{contractor.name}</Table.Cell>
                                        <Table.Cell {...commonCellProps}
                                                    width={"15%"}>{contractor.taxNumber}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"25%"}>
                                            {contractor.street} {contractor.buildingNo}
                                            {contractor.apartmentNo && contractor.apartmentNo.trim() !== ""
                                                ? "/" + contractor.apartmentNo
                                                : ""}
                                            , {contractor.postalCode} {contractor.city},{" "}
                                            {contractor.country &&
                                            typeof contractor.country === "object"
                                                ? contractor.country.name
                                                : contractor.country || ""}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"4%"}>
                                            {contractor.customer ? t("common:yes") : t("common:no")}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"4%"}>
                                            {contractor.supplier ? t("common:yes") : t("common:no")}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"4%"}>
                                            {contractor.scaffoldingUser ? t("common:yes") : t("common:no")}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"}
                                                    fontSize={"x-small"}>
                                            {DateFormatter.formatDateTime(contractor.createdDatetime!)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                            <Box>
                                                {contractor.createdBy.firstName.charAt(0)}. {contractor.createdBy.lastName}
                                            </Box>
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"}
                                                    fontSize={"x-small"}>
                                            {DateFormatter.formatDateTime(contractor.lastModifiedDatetime!)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                            <Box>
                                                {contractor.modifiedBy.firstName.charAt(0)}. {contractor.modifiedBy.lastName}
                                            </Box>
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}
                                                    onClick={(e) => e.stopPropagation()}
                                        >
                                            <HStack gap={1} justifyContent={"center"}>
                                                <EditContractorDialog
                                                    fetchContractors={fetchContractors}
                                                    contractorId={contractorId}
                                                />
                                                <Button
                                                    colorPalette="orange"
                                                    size="2xs"
                                                    onClick={() => onDelete(contractorId)}
                                                >
                                                    {t("delete", {ns: "common"})}
                                                </Button>
                                            </HStack>
                                        </Table.Cell>
                                    </Table.Row>

                                    {/* Wiersz rozwijany z tabelą kontaktów */}
                                    {expandedRows[contractorId] && (
                                        <Table.Row
                                            bg={themeColors.bgColorSecondary}
                                            _hover={{
                                                textDecoration: 'none',
                                                bg: themeColors.highlightBgColor,
                                                color: themeColors.fontColorHover
                                            }}
                                        >
                                            <Table.Cell colSpan={12} {...commonCellProps}>
                                                <Collapsible.Root open={expandedRows[contractorId]}>
                                                    <Collapsible.Content>
                                                        <Box
                                                            maxH="300px"
                                                            overflowY="auto"
                                                            onScroll={(e) => {
                                                                const target = e.target as HTMLElement;
                                                                if (
                                                                    target.scrollHeight - target.scrollTop <=
                                                                    target.clientHeight + 20
                                                                ) {
                                                                    loadMoreContacts(contractorId);
                                                                }
                                                            }}>
                                                            <GenericContactTable
                                                                contacts={contractor.contacts!!}
                                                                onDelete={contactOnDelete}
                                                                fetchContacts={contactFetchContacts}
                                                                onSortChange={contactOnSortChange}
                                                                sortField={contactSortField}
                                                                sortDirection={contactSortDirection}
                                                                extended={false}
                                                            />
                                                            {contactState?.loading && <Spinner size="sm" mt={2} />}
                                                        </Box>
                                                    </Collapsible.Content>
                                                </Collapsible.Root>
                                            </Table.Cell>
                                        </Table.Row>
                                    )}
                                </React.Fragment>
                            );
                        })}
                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>
        </Box>
    );
};

export default ContractorTableWithContacts;
