import {FetchableScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";
import {useTranslation} from "react-i18next";
import {Badge, Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import React, {useCallback, useEffect, useRef, useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import AuditCell from "@/components/shared/AuditCell.tsx";
import {FaCirclePlus, FaExplosion, FaRegTrashCan} from "react-icons/fa6";
import AddScaffoldingLogPositionDialog from "@/components/scaffolding/position/AddScaffoldingLogPositionDialog.tsx";
import ScaffoldingLogPositionDetailsDialog from "./ScaffoldingLogPositionDetailsDialog";
import {buildScaffoldingTree} from "@/utils/scaffolding-tree-builder";
import EditScaffoldingLogPositionDialog from "@/components/scaffolding/position/EditScaffoldingLogPositionDialog.tsx";
import TechnicalProtocolDialog from "@/components/scaffolding/protocol/TechnicalProtocolDialog.tsx";


function useOutsideClick(
    ref: React.RefObject<HTMLElement | null>,
    callback: () => void
) {
    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            const target = event.target as Element;

            const isInsidePortalOrModal = target.closest(
                '[role="dialog"], [role="alertdialog"], [role="menu"], [role="listbox"], [data-part="content"]'
            );

            if (ref.current && !ref.current.contains(target) && !isInsidePortalOrModal) {
                callback();
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [ref, callback]);
}

interface ScaffoldingLogPositionTableProps {
    positions: FetchableScaffoldingLogPositionDTO[];
    onDelete: (id: number) => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    fetchPositions: () => void;
    currentPage: number;
    rowsPerPage: number;
}

const ScaffoldingLogPositionTable: React.FC<ScaffoldingLogPositionTableProps> = ({
                                                                                     positions,
                                                                                     onDelete,
                                                                                     onSortChange,
                                                                                     sortField,
                                                                                     sortDirection,
                                                                                     fetchPositions,
                                                                                     currentPage,
                                                                                     rowsPerPage
                                                                                 }) => {
    const {t} = useTranslation(['scaffoldingLogPositions', 'common',
        'technicalProtocolStatuses', 'scaffoldingTypes',
        'scaffoldingOperationTypes', 'dimensionDescriptions', 'dimensionTypes',
        'employees', 'units']);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedId, setSelectedId] = useState<number | null>(null);

    const [expandedRows, setExpandedRows] = useState<Set<number>>(new Set());

    const tableRef = useRef<HTMLDivElement>(null);

    const themeColors = useThemeColors();
    const {commonColumnHeaderProps} = useTableStyles();

    const treePositions = React.useMemo(() => {
        return buildScaffoldingTree(positions);
    }, [positions]);


    // HANDLER ROZWIJANIA/ZWIJANIA
    const toggleRow = useCallback((id: number) => {
        setExpandedRows(prev => {
            const newSet = new Set(prev);
            if (newSet.has(id)) {
                newSet.delete(id);
            } else {
                newSet.add(id);
            }
            return newSet;
        });
    }, []);

    // ZWIJANIE WSZYSTKIEGO PO KLIKNIĘCIU POZA
    useOutsideClick(tableRef, () => {
        if (expandedRows.size > 0) {
            setExpandedRows(new Set());
        }
    });

    const handleDeleteClick = (id: number) => {
        setSelectedId(id);
        onOpen();
    }

    const confirmDelete = () => {
        if (selectedId) {
            onDelete(selectedId);
            onClose();
        }
    };

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    }

    if (!treePositions || treePositions.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound", {ns: "common"})}</Text>
            </Field>
        );
    }

    return (
        <>
            <div ref={tableRef}>

                <Table.ScrollArea height="auto" borderWidth="1px" borderRadius="md" borderColor="grey">
                    <Table.Root size="sm" interactive showColumnBorder color={themeColors.fontColor}
                                style={{tableLayout: "fixed"}}>
                        <Table.Header>
                            <Table.Row bg={themeColors.bgColorPrimary}>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width="20px"/>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"2%"}
                                                    onClick={() => onSortChange("id")}>
                                    Lp.{renderSortIndicator("id")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"8%"}
                                                    onClick={() => onSortChange("scaffoldingNumber")}>
                                    {t("scaffoldingLogPositions:scaffoldingNumber")}{renderSortIndicator("scaffoldingNumber")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"15%"}
                                                    onClick={() => onSortChange("assemblyLocation")}>
                                    {t("scaffoldingLogPositions:assemblyLocation")}{renderSortIndicator("assemblyLocation")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("assemblyDate")}>
                                    {t("scaffoldingLogPositions:assemblyDate")}{renderSortIndicator("assemblyDate")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("dismantlingDate")}>
                                    {t("scaffoldingLogPositions:dismantlingDate")}{renderSortIndicator("dismantlingDate")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("dismantlingNotificationDate")}>
                                    {t("scaffoldingLogPositions:dismantlingNotificationDateShort")}{renderSortIndicator("dismantlingNotificationDate")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("scaffoldingType")}>
                                    {t("scaffoldingLogPositions:scaffoldingType")}{renderSortIndicator("scaffoldingType")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("scaffoldingFullDimension")}>
                                    {t("scaffoldingLogPositions:scaffoldingFullDimension")}{renderSortIndicator("scaffoldingFullDimension")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("scaffoldingPartialDimension")}>
                                    {t("scaffoldingLogPositions:scaffoldingPartialDimension")}{renderSortIndicator("scaffoldingPartialDimension")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("fullWorkingTime")}>
                                    {t("scaffoldingLogPositions:fullWorkingTime")}{renderSortIndicator("fullWorkingTime")}</Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps} width={"5%"}
                                                    onClick={() => onSortChange("technicalProtocolStatus")}>
                                    {t("scaffoldingLogPositions:technicalProtocolStatus")}{renderSortIndicator("technicalProtocolStatus")}</Table.ColumnHeader>
                                <Table.ColumnHeader
                                    {...commonColumnHeaderProps} width={"6%"}
                                    onClick={() => onSortChange("createdDatetime")}
                                >
                                    {t("common:created")} {renderSortIndicator("createdDatetime")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader
                                    {...commonColumnHeaderProps} width={"6%"}
                                    onClick={() => onSortChange("lastModifiedDatetime")}
                                >
                                    {t("common:lastModified")} {renderSortIndicator("lastModifiedDatetime")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader {...commonColumnHeaderProps}>{t("actions")}</Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {treePositions.map((position, index) => (
                                <ScaffoldingLogPositionRow
                                    key={position.id}
                                    position={position}
                                    index={index}
                                    currentPage={currentPage}
                                    rowsPerPage={rowsPerPage}
                                    fetchPositions={fetchPositions}
                                    onDeleteClick={handleDeleteClick}
                                    t={t}
                                    themeColors={themeColors}
                                    level={0}
                                    expandedRows={expandedRows}
                                    onToggle={toggleRow}
                                />
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
            </div>
        </>
    )
}

// --- KOMPONENT WIERSZA (REKURENCYJNY) ---

interface RowProps {
    position: FetchableScaffoldingLogPositionDTO;
    index: number;
    currentPage: number;
    rowsPerPage: number;
    fetchPositions: () => void;
    onDeleteClick: (id: number) => void;
    t: any;
    themeColors: any;
    level: number;
    parentIndexString?: string;
    expandedRows: Set<number>;
    onToggle: (id: number) => void;
}

const ScaffoldingLogPositionRow: React.FC<RowProps> = ({
                                                           position,
                                                           index,
                                                           currentPage,
                                                           rowsPerPage,
                                                           fetchPositions,
                                                           onDeleteClick,
                                                           t,
                                                           themeColors,
                                                           level,
                                                           parentIndexString,
                                                           expandedRows,
                                                           onToggle
                                                       }) => {
    const {commonCellProps} = useTableStyles();

    const isExpanded = position.id ? expandedRows.has(position.id) : false;
    const hasChildren = position.mappedChildPositions && position.mappedChildPositions.length > 0;

    const currentIndexString = React.useMemo(() => {
        // Dla poziomu 0 (głównego): (strona * wiersze) + index + 1
        if (level === 0) {
            return ((currentPage * rowsPerPage) + index + 1).toString();
        }
        // Dla dzieci: "LpRodzica.LpDziecka"
        // Używamy (index + 1), żeby dzieci były numerowane od 1
        return `${parentIndexString}.${index + 1}`;
    }, [level, currentPage, rowsPerPage, index, parentIndexString]);

    const handleRowClick = (e: React.MouseEvent) => {
        e.stopPropagation();
        if (hasChildren && position.id) {
            onToggle(position.id);
        }
    };

    const paddingLeft = level * 20;

    return (
        <React.Fragment>
            <Table.Row
                bg={level > 0 ? themeColors.bgColorPrimary : themeColors.bgColorSecondary}
                _hover={hasChildren || level > 0 ? {
                    bg: themeColors.highlightBgColor,
                    cursor: "pointer"
                } : undefined}
                onClick={handleRowClick}
            >
                {/* Kolumna strzałki */}
                <Table.Cell {...commonCellProps}
                            paddingLeft={`${paddingLeft}px`}>
                    {hasChildren && (
                        <Box transform={isExpanded ? "rotate(90deg)" : "rotate(0deg)"} transition="transform 0.2s">
                            ▶
                        </Box>
                    )}
                </Table.Cell>

                <Table.Cell {...commonCellProps} >
                    {currentIndexString}
                </Table.Cell>

                <Table.Cell {...commonCellProps}>
                    {hasChildren && <Badge colorPalette="blue" mr={1} size="xs">P</Badge>}
                    {position.scaffoldingNumber}
                </Table.Cell>

                <Table.Cell {...commonCellProps}>{position.assemblyLocation}</Table.Cell>
                <Table.Cell {...commonCellProps}>{DateFormatter.formatDate(position.assemblyDate)}</Table.Cell>
                <Table.Cell {...commonCellProps}>{DateFormatter.formatDate(position.dismantlingDate)}</Table.Cell>
                <Table.Cell {...commonCellProps}>{DateFormatter.formatDate(position.dismantlingNotificationDate)}</Table.Cell>
                <Table.Cell {...commonCellProps} fontSize = "x-small" >{t(`scaffoldingTypes:${position.scaffoldingType}`)}</Table.Cell>
                <Table.Cell {...commonCellProps}>
                    {position.scaffoldingFullDimension} {position.scaffoldingFullDimensionUnit?.symbol}
                </Table.Cell>
                <Table.Cell {...commonCellProps}>
                    {position.scaffoldingPartialDimension} {position.scaffoldingPartialDimensionUnit?.symbol}
                </Table.Cell>
                <Table.Cell {...commonCellProps}>{position.partialWorkingTime} r-h</Table.Cell>
                <Table.Cell {...commonCellProps}>
                    {t(`technicalProtocolStatuses:${position.technicalProtocolStatus}`)}
                </Table.Cell>

                <AuditCell
                    value={position.createdDatetime}
                    user={position.createdBy}
                    cellProps={commonCellProps}
                />
                <AuditCell
                    value={position.lastModifiedDatetime}
                    user={position.modifiedBy}
                    cellProps={commonCellProps}
                />

                <Table.Cell {...commonCellProps} onClick={(e) => e.stopPropagation()}>
                    <HStack gap={1} flexWrap={"wrap"}>
                        <ScaffoldingLogPositionDetailsDialog position={position}/>

                        {/* Dodawanie podpozycji: przekazujemy ID rodzica */}
                        <AddScaffoldingLogPositionDialog
                            fetchPositions={fetchPositions}
                            scaffoldingLogId={position.scaffoldingLog?.id}
                            parentPosition={position.parentPosition ? position.parentPosition : position}
                            triggerLabel={t("addSubPosition", {ns: "scaffoldingLogPositions"})}
                            triggerIcon={<FaCirclePlus/>}
                            triggerColorPalette="orange"
                        />
                        <TechnicalProtocolDialog position={position}/>
                        <EditScaffoldingLogPositionDialog
                            positionId={position.id!}
                            fetchPositions={fetchPositions}
                        />

                        <Button colorPalette="red" size="2xs" onClick={() => onDeleteClick(position.id!)}>
                            <FaExplosion/>
                            <FaRegTrashCan/>
                            {t("delete", {ns: "common"})}
                        </Button>
                    </HStack>
                </Table.Cell>
            </Table.Row>

            {/* REKURENCYJNE WYŚWIETLANIE DZIECI */}
            {isExpanded && hasChildren && position.mappedChildPositions?.map((child, childIndex) => (
                <ScaffoldingLogPositionRow
                    key={child.id}
                    position={child}
                    index={childIndex}
                    currentPage={currentPage}
                    rowsPerPage={rowsPerPage}
                    fetchPositions={fetchPositions}
                    onDeleteClick={onDeleteClick}
                    t={t}
                    themeColors={themeColors}
                    level={level + 1}
                    parentIndexString={currentIndexString}
                    expandedRows={expandedRows}
                    onToggle={onToggle}
                />
            ))}
        </React.Fragment>
    );
};

export default ScaffoldingLogPositionTable;