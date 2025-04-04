import {FetchableContractorDTO} from "@/types/contractor-types.ts";
import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import React, {useState} from "react";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import '@/theme/css/global-table-styles.css';
import '@/theme/css/contractor-table-styles.css';
import EditContractorDrawer from "@/components/contractor/EditContractorDrawer.tsx";
import EditContractorDialog from "@/components/contractor/EditContractorDialog.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";

//left this component...will be changed to genericContractorTable and used in ContactTableWithContractors in future.

interface Props {
    contractors: FetchableContractorDTO[];
    onDelete: (id: number) => void;
    fetchContractors: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc"
}

const ContractorTable: React.FC<Props> = ({
                                              contractors,
                                              onDelete,
                                              fetchContractors,
                                              onSortChange,
                                              sortField,
                                              sortDirection
                                          }) => {
    const {t} = useTranslation(['common', 'contractors']);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedContractorId, setSelectedContractorId] = useState<number | null>(null);
    const themeColors = useThemeColors();


    const handleDeleteClick = (id: number) => {
        setSelectedContractorId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedContractorId !== null) {
            onDelete(selectedContractorId);
        }
        onClose();
    };

    if (!contractors || contractors.length === 0) {
        return (
            <Field alignContent={"center"}>
                <Text fontSize={20}>{t('dataNotFound')}</Text>
            </Field>)
    }

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    return (
        <Box>
            <Table.Root size={"sm"}
                        interactive
                        showColumnBorder
                        color={themeColors.fontColor}>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("id")}>
                            ID {renderSortIndicator("id")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("name")}>
                            {t('contractors:name')} {renderSortIndicator("name")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("taxNumber")}>
                            {t('contractors:taxNumber')}{renderSortIndicator("taxNumber")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("street")}>
                            {t('contractors:address')}{renderSortIndicator("street")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("customer")}>
                            {t('contractors:customer')}{renderSortIndicator("customer")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("supplier")}>
                            {t('contractors:supplier')}{renderSortIndicator("supplier")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("scaffoldingUser")}>
                            {t('contractors:scaffoldingUser')}{renderSortIndicator("scaffoldingUser")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("createdDatetime")}>
                            {t('createDate')}{renderSortIndicator("createdDatetime")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("createdBy")}>
                            {t('createdBy')}{renderSortIndicator("createdBy")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("lastModifiedDatetime")}>
                            {t('lastModifiedDate')}{renderSortIndicator("lastModifiedDatetime")}</Table.ColumnHeader>
                        <Table.ColumnHeader cursor={"pointer"}
                                            onClick={() => onSortChange("modifiedBy")}>
                            {t('lastModifiedBy')}{renderSortIndicator("modifiedBy")}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('edit')}</Table.ColumnHeader>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {contractors.map((contractor) => (
                        <Table.Row key={contractor.id}>
                            <Table.Cell>{contractor.id}</Table.Cell>
                            <Table.Cell>{contractor.name}</Table.Cell>
                            <Table.Cell>{contractor.taxNumber}</Table.Cell>
                            <Table.Cell>
                                <div>
                                    {contractor.street} {contractor.buildingNo}
                                    {contractor.apartmentNo && contractor.apartmentNo.trim() !== ""
                                        ? "/" + contractor.apartmentNo
                                        : ""},{" "}
                                    {contractor.postalCode} {contractor.city},{" "}
                                    {contractor.country && typeof contractor.country === "object"
                                        ? contractor.country.name
                                        : contractor.country || ""}
                                </div>
                            </Table.Cell>
                            <Table.Cell>{contractor.customer ? t("common:yes") : t("common:no")}</Table.Cell>
                            <Table.Cell>{contractor.supplier ? t("common:yes") : t("common:no")}</Table.Cell>
                            <Table.Cell>{contractor.scaffoldingUser ? t("common:yes") : t("common:no")}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(contractor.createdDatetime!)}</Table.Cell>
                            <Table.Cell>
                                <div>{contractor.createdByFirstName}</div>
                                <div>{contractor.createdByLastName}</div>
                            </Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(contractor.lastModifiedDatetime!)}</Table.Cell>
                            <Table.Cell>
                                <div>{contractor.modifiedByFirstName}</div>
                                <div>{contractor.modifiedByLastName}</div>
                            </Table.Cell>
                            <Table.Cell>
                                <HStack gap={1} alignContent={"center"}>
                                    <EditContractorDrawer
                                        fetchContractors={fetchContractors}
                                        contractorId={contractor.id!}/>
                                    <EditContractorDialog fetchContractors={fetchContractors}
                                                          contractorId={contractor.id!}/>
                                    <Button
                                        colorPalette="red"
                                        size={"2xs"}
                                        onClick={() => handleDeleteClick(contractor.id!)}>
                                        {t('delete', {ns: "common"})}
                                    </Button>
                                </HStack>
                            </Table.Cell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table.Root>

            <ConfirmModal
                isOpen={open}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t("deleteConfirmation.title", {ns: "common"})}
                message={t("deleteConfirmation.message", {ns: "common"})}
                confirmText={t("delete", {ns: "common"})}
                cancelText={t("cancel", {ns: "common"})}
            />
        </Box>
    )

}


export default ContractorTable;