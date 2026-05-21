import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import AuditCell from "@/components/shared/AuditCell.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {FetchableUnitDTO, UnitBaseDTO, UnitDTO} from "@/types/unit-types.ts";
import EditUnitDrawer from "@/components/unit/EditUnitDrawer.tsx";

function isFechtableUnit(
    unit: UnitBaseDTO
): unit is FetchableUnitDTO {
    return "createdDatetime" in unit;
}

interface Props<T extends UnitBaseDTO> {
    units: T[];
    onDelete: (id: number) => void;
    fetchUnits: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean;
}

const GenericUnitTable = <T extends UnitDTO>({
                                                 units,
                                                 fetchUnits,
                                                 onDelete,
                                                 onSortChange,
                                                 sortField,
                                                 sortDirection,
                                                 extended = false,
                                             }: Props<T>) => {
    const {t} = useTranslation(["common", "units"]);
    const {open, onOpen, onClose} = useDisclosure();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [selectedUnitId, setSelectedUnitId] = useState<number | null>(null);
    const themeColors = useThemeColors();

    const handleDeleteClick = (id: number) => {
        setSelectedUnitId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedUnitId !== null) {
            onDelete(selectedUnitId);
        }
        onClose();
    };

    const renderSortIndicator = (field: string) => {

        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!units || units.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound")}</Text>
            </Field>
        );
    }

    return (
        <Box>
            <Table.ScrollArea height={"auto"} borderWidth={"1px"} borderRadius={"md"} borderColor={"grey"}>
                <Table.Root size={"sm"}
                            interactive
                            showColumnBorder
                            color={themeColors.fontColor}
                >
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("id")}
                            >
                                ID {renderSortIndicator("id")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("symbol")}
                            >
                                {t("units:symbol")} {renderSortIndicator("symbol")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("description")}
                            >
                                {t("units:description")} {renderSortIndicator("description")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("unitType")}
                            >
                                {t("units:unitType")} {renderSortIndicator("unitType")}
                            </Table.ColumnHeader>
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
                        {units.map((unit) => {
                            console.log(unit)
                            return (
                                <Table.Row key={unit.id}
                                           bg={themeColors.bgColorSecondary}
                                           _hover={{
                                               textDecoration: 'none',
                                               bg: themeColors.highlightBgColor,
                                               color: themeColors.fontColorHover
                                           }}
                                >
                                    <Table.Cell {...commonCellProps} width={"2%"}>{unit.id}</Table.Cell>
                                    <Table.Cell {...commonCellProps} width={"30%"}>{unit.symbol}</Table.Cell>
                                    <Table.Cell {...commonCellProps} width={"30%"}>{unit.description}</Table.Cell>
                                    <Table.Cell {...commonCellProps}
                                                width={"30%"}>{t(`units:unitTypes.${unit.unitType}`)}</Table.Cell>
                                    {extended && isFechtableUnit(unit) && (
                                        <>
                                            <AuditCell
                                                value={unit.createdDatetime}
                                                user={unit.createdBy}
                                                cellProps={commonCellProps}
                                                width={"15%"}
                                            />
                                            <AuditCell
                                                value={unit.lastModifiedDatetime}
                                                user={unit.modifiedBy}
                                                cellProps={commonCellProps}
                                                width={"15%"}
                                            />
                                        </>
                                    )}
                                    {extended && (
                                        <Table.Cell {...commonCellProps}>
                                            <HStack gap={1} justifyContent="center">
                                                {(() => {
                                                    const isSystemUnit = unit.company === null || unit.company.id === null;
                                                    return (
                                                        <>
                                                            <EditUnitDrawer
                                                                fetchUnits={fetchUnits}
                                                                unitId={unit.id!}
                                                                disabled={isSystemUnit}
                                                            />
                                                            <Button
                                                                colorPalette="orange"
                                                                size="2xs"
                                                                onClick={() => handleDeleteClick(unit.id!)}
                                                                disabled={isSystemUnit}
                                                            >
                                                                {t("delete", {ns: "common"})}
                                                            </Button>
                                                        </>
                                                    );
                                                })()}
                                            </HStack>
                                        </Table.Cell>
                                    )}
                                </Table.Row>
                            )
                        })}
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

export default GenericUnitTable;
