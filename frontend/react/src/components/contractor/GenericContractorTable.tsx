import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {Field} from "@/components/ui/field.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import EditContractorDialog from "@/components/contractor/EditContractorDialog.tsx";
import {ContractorBaseDTO, ContractorDTO, FetchableContractorDTO} from "@/types/contractor-types.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {useState} from "react";
import AuditCell from "@/components/shared/AuditCell.tsx";

function isFetchableContractor(
    contractor: ContractorBaseDTO
): contractor is FetchableContractorDTO {
    return "createDatetime" in contractor;
}

interface Props<T extends ContractorBaseDTO> {
    contractors: T[];
    onDelete: (id: number) => void;
    fetchContractors: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean;
}


const GenericContractorTable = <T extends ContractorDTO>({
                                                             contractors,
                                                             onDelete,
                                                             fetchContractors,
                                                             onSortChange,
                                                             sortField,
                                                             sortDirection,
                                                             extended = false,
                                                         }: Props<T>) => {
    const {t} = useTranslation(["common", "contractors"]);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedContractorId, setSelectedContractorId] = useState<number | null>(null);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    const handleDeleteClick = (id: number) => {
        setSelectedContractorId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedContractorId !== null && onDelete) {
            onDelete(selectedContractorId);
        }
        onClose();
    };

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!contractors || contractors.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound")}</Text>
            </Field>
        );
    }

    return (
        <Box>
            <Table.ScrollArea height={"auto"} borderWidth={"1px"} borderRadius={"md"} borderColor={"grey"}>
                <Table.Root size="sm" interactive showColumnBorder color={themeColors.fontColor}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("id")}>ID {renderSortIndicator("id")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("name")}>{t("contractors:name")} {renderSortIndicator("name")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("taxNumber")}>{t("contractors:taxNumber")} {renderSortIndicator("taxNumber")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("street")}>{t("contractors:address")} {renderSortIndicator("street")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("customer")}>{t('contractors:customer')}{renderSortIndicator("customer")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("supplier")
                                                }>{t('contractors:supplier')}{renderSortIndicator("supplier")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("scaffoldingUser")}>{t('contractors:scaffoldingUser')}{renderSortIndicator("scaffoldingUser")}</Table.ColumnHeader>
                            {extended && (
                                <>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("createdDatetime")}
                                    >
                                        {t("created")} {renderSortIndicator("createdDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("lastModifiedDatetime")}
                                    >
                                        {t("lastModified")} {renderSortIndicator("lastModifiedDatetime")}
                                    </Table.ColumnHeader>

                                    <Table.ColumnHeader {...commonColumnHeaderProps}
                                    >{t("edit")}
                                    </Table.ColumnHeader>
                                </>
                            )}
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {contractors.map(contractor => (
                            <Table.Row key={contractor.id} bg={themeColors.bgColorSecondary}
                                       _hover={{bg: themeColors.highlightBgColor, color: themeColors.fontColorHover}}>
                                <Table.Cell {...commonCellProps}>{contractor.id}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contractor.name}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contractor.taxNumber}</Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {contractor.street} {contractor.buildingNo}
                                    {contractor.apartmentNo ? `/${contractor.apartmentNo}` : ""}, {contractor.postalCode} {contractor.city}, {typeof contractor.country === "object" ? contractor.country.name : contractor.country || ""}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>{contractor.customer ? t("common:yes") : t("common:no")}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contractor.supplier ? t("common:yes") : t("common:no")}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contractor.scaffoldingUser ? t("common:yes") : t("common:no")}</Table.Cell>
                                {extended && isFetchableContractor(contractor) && (
                                    <>
                                        <AuditCell
                                            value={contractor.createdDatetime}
                                            user={contractor.createdBy}
                                            cellProps={commonCellProps}
                                        />
                                        <AuditCell
                                            value={contractor.lastModifiedDatetime}
                                            user={contractor.modifiedBy}
                                            cellProps={commonCellProps}
                                        />
                                        <Table.Cell {...commonCellProps}>
                                            <HStack gap={1} justifyContent={"center"}>
                                                <EditContractorDialog
                                                    fetchContractors={fetchContractors}
                                                    contractorId={contractor.id!}
                                                />
                                                <Button
                                                    colorPalette="orange"
                                                    size="2xs"
                                                    onClick={() => onDelete(contractor.id!)}
                                                >
                                                    {t("delete", {ns: "common"})}
                                                </Button>
                                            </HStack>
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            <HStack gap={1} justifyContent="center">
                                                {fetchContractors &&
                                                    <EditContractorDialog fetchContractors={fetchContractors}
                                                                          contractorId={contractor.id!}/>}
                                                <Button colorPalette="orange" size="2xs"
                                                        onClick={() => handleDeleteClick(contractor.id!)}>
                                                    {t("delete", {ns: "common"})}
                                                </Button>

                                            </HStack>
                                        </Table.Cell>
                                    </>
                                )}{
                            }
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
        </Box>
    );
};

export default GenericContractorTable;