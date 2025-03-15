// ContractorTableWithContacts.tsx
import React, {useState} from "react";
import {Box, Button, Collapsible, HStack, Table, Text,} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import DateFormatter from "@/utils/date-formatter";
import EditContractorDialog from "@/components/contractor/EditContractorDialog";
import {FetchableContractorDTO} from "@/types/contractor-types";
import GenericContactTable from "@/components/contact/GenericContactTable.tsx";
import {themeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";


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
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    // Stan rozwinięcia wierszy, przyjmujemy, że contractor.id jest zawsze zdefiniowane
    const [expandedRows, setExpandedRows] = useState<{ [key: number]: boolean }>({});

    const toggleExpand = (id: number) => {
        setExpandedRows((prev) => ({...prev, [id]: !prev[id]}));
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
                            stickyHeader
                            showColumnBorder
                            interactive
                            color={themeColors.fontColor()}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary()}>
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
                            return (
                                <React.Fragment key={contractorId}>
                                    <Table.Row
                                        bg={themeColors.bgColorSecondary()}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor(),
                                            color: themeColors.fontColorHover()
                                        }}
                                        onClick={() => toggleExpand(contractorId)} style={{cursor: "pointer"}}>
                                        <Table.Cell {...commonCellProps}>{contractorId}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{contractor.name}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{contractor.taxNumber}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>
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
                                        <Table.Cell {...commonCellProps}>
                                            {contractor.customer ? t("common:yes") : t("common:no")}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {contractor.supplier ? t("common:yes") : t("common:no")}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {contractor.scaffoldingUser ? t("common:yes") : t("common:no")}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>{DateFormatter.formatDateTime(contractor.createdDatetime!)}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            <Box>{contractor.createdByFirstName}</Box>
                                            <Box>{contractor.createdByLastName}</Box>
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>{DateFormatter.formatDateTime(contractor.lastModifiedDatetime!)}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            <Box>{contractor.modifiedByFirstName}</Box>
                                            <Box>{contractor.modifiedByLastName}</Box>
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} onClick={(e) => e.stopPropagation()}>
                                            <HStack gap={1}>
                                                <EditContractorDialog
                                                    fetchContractors={fetchContractors}
                                                    contractorId={contractorId}
                                                />
                                                <Button
                                                    colorPalette="red"
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
                                            bg={themeColors.bgColorSecondary()}
                                            _hover={{
                                                textDecoration: 'none',
                                                bg: themeColors.highlightBgColor(),
                                                color: themeColors.fontColorHover()
                                            }}
                                        >
                                            <Table.Cell colSpan={13} {...commonCellProps}>
                                                <Collapsible.Root open={expandedRows[contractorId]}>
                                                    <Collapsible.Content>
                                                        <Box>
                                                            <GenericContactTable
                                                                contacts={contractor.contacts}
                                                                onDelete={contactOnDelete}
                                                                fetchContacts={contactFetchContacts}
                                                                onSortChange={contactOnSortChange}
                                                                sortField={contactSortField}
                                                                sortDirection={contactSortDirection}
                                                                extended={false}
                                                            />
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
