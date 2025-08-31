import {FetchableConstructionSiteDTO} from "@/types/constrution-site-types.ts";
import React, {useState} from "react";
import {Box, Button, Table, Text, useDisclosure} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import { useTranslation } from "react-i18next";


interface ConstructionSiteTableProps {
    constructionSites: FetchableConstructionSiteDTO[];
    onDelete: (id: number) => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
}

const ConstructionSiteTable: React.FC<ConstructionSiteTableProps> = ({
                                                                         constructionSites,
                                                                         onDelete,
                                                                         onSortChange,
                                                                         sortField,
                                                                         sortDirection
                                                                     }) => {
    const {t} = useTranslation('constructionSites');
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedConstructionSiteId, setSelectedConstructionSiteId] = useState<number | null>(null);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    const handleDeleteClick = (id: number) => {
        setSelectedConstructionSiteId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedConstructionSiteId !== null) {
            onDelete(selectedConstructionSiteId);
        }
        onClose();
    }

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    }

    if (!constructionSites || constructionSites.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound", {ns: "common"})}</Text>
            </Field>
        );
    }


    return (
        <>
            <Table.ScrollArea height="auto" borderWidth="1px" borderRadius="md" borderColor="grey">
                <Table.Root size="sm" interactive showColumnBorder color={themeColors.fontColor}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("id")}>
                                ID{renderSortIndicator("id")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("name")}>
                                {t("name")} {renderSortIndicator("name")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("address")}>
                                {t("address")} {renderSortIndicator("address")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("country")}>
                                {t("country")} {renderSortIndicator("country")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("shortName")}>
                                {t("shortName")} {renderSortIndicator("shortName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("code")}>
                                {t("code")} {renderSortIndicator("code")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("createdDatetime")}
                            >
                                {t("createDate")} {renderSortIndicator("createdDatetime")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("createdBy")}
                            >
                                {t("createdBy")} {renderSortIndicator("createdBy")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("lastModifiedDatetime")}
                            >
                                {t("lastModifiedDate")} {renderSortIndicator("lastModifiedDatetime")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("modifiedBy")}
                            >
                                {t("lastModifiedBy")} {renderSortIndicator("modifiedBy")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                            >{t("edit")}
                            </Table.ColumnHeader>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {constructionSites.map((cs) => (
                            <Table.Row
                                key={cs.id}
                                bg={themeColors.bgColorSecondary}
                                _hover={{
                                    textDecoration: "none",
                                    bg: themeColors.highlightBgColor,
                                    color: themeColors.fontColorHover,
                                }}
                            >
                                <Table.Cell {...commonCellProps}>{cs.id}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{cs.name}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{cs.address}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{cs.country.name}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{cs.shortName}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{cs.code}</Table.Cell>
                                {"createdDatetime" in cs && (
                                    <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                        {DateFormatter.formatDateTime(
                                            (cs as FetchableConstructionSiteDTO).createdDatetime
                                        )}
                                    </Table.Cell>
                                )}
                                {"createdBy" in cs && (
                                    <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                        <Box>
                                            {(cs as FetchableConstructionSiteDTO).createdBy.firstName.charAt(0)}. {(cs as FetchableConstructionSiteDTO).createdBy.lastName}
                                        </Box>
                                    </Table.Cell>
                                )}
                                {"lastModifiedDatetime" in cs && (
                                    <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                        {DateFormatter.formatDateTime(
                                            (cs as FetchableConstructionSiteDTO).lastModifiedDatetime
                                        )}
                                    </Table.Cell>
                                )}
                                {"modifiedBy" in cs && (
                                    <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                        <Box>
                                            {(cs as FetchableConstructionSiteDTO).modifiedBy.firstName.charAt(0)}. {(cs as FetchableConstructionSiteDTO).modifiedBy.lastName}
                                        </Box>
                                    </Table.Cell>
                                )}
                                <Table.Cell {...commonCellProps}>
                                    <Button colorPalette="red" size="2xs" onClick={() => handleDeleteClick(cs.id!)}>
                                        {t("delete", {ns: "common"})}
                                    </Button>
                                </Table.Cell>
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>

            <ConfirmModal
                isOpen={open}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t("deleteConfirmation.title", {ns: "common"})}
                message={t("deleteConfirmation.message", {ns: "common"})}
                confirmText={t("delete", {ns: "common"})}
                cancelText={t("cancel", {ns: "common"})}
            />
        </>
    )
}

export default ConstructionSiteTable;