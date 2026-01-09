import {FetchableScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";
import {useTranslation} from "react-i18next";
import {Badge, Box, Button, Collapsible, HStack, Table, Text, useDisclosure, VStack} from "@chakra-ui/react";
import React, {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import AuditCell from "@/components/shared/AuditCell.tsx";
import {FaCirclePlus, FaExplosion, FaPen, FaRegTrashCan} from "react-icons/fa6";
import AddScaffoldingLogPositionDialog from "@/components/scaffolding/position/AddScaffoldingLogPositionDialog.tsx";

interface ScaffoldingLogPositionTableProps {
    positions: FetchableScaffoldingLogPositionDTO[];
    onDelete: (id: number) => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    fetchPositions: () => void;
}

const ScaffoldingLogPositionTable: React.FC<ScaffoldingLogPositionTableProps> = ({
                                                                                     positions,
                                                                                     onDelete,
                                                                                     onSortChange,
                                                                                     sortField,
                                                                                     sortDirection,
                                                                                     fetchPositions
                                                                                 }) => {
    const {t} = useTranslation(['scaffoldingLogPositions', 'common',
        'technicalProtocolStatuses', 'scaffoldingTypes',
        'scaffoldingOperationTypes', 'dimensionDescriptions', 'dimensionTypes',
        'employees', 'units']);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedId, setSelectedId] = useState<number | null>(null);
    const [expandedRow, setExpandedRow] = useState<number | null>(null);

    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    const toggleExpand = (id: number) => {
        console.log(positions);
        setExpandedRow(prev => (prev === id ? null : id));
    };

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

    if (!positions || positions.length === 0) {
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
                                                onClick={() => onSortChange("scaffoldingNumber")}>
                                {t("scaffoldingLogPositions:scaffoldingNumber")}{renderSortIndicator("scaffoldingNumber")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("assemblyLocation")}>
                                {t("scaffoldingLogPositions:assemblyLocation")}{renderSortIndicator("assemblyLocation")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("assemblyDate")}>
                                {t("scaffoldingLogPositions:assemblyDate")}{renderSortIndicator("assemblyDate")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("dismantlingDate")}>
                                {t("scaffoldingLogPositions:dismantlingDate")}{renderSortIndicator("dismantlingDate")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("dismantlingNotificationDate")}>
                                {t("scaffoldingLogPositions:dismantlingNotificationDateShort")}{renderSortIndicator("dismantlingNotificationDate")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("scaffoldingType")}>
                                {t("scaffoldingLogPositions:scaffoldingType")}{renderSortIndicator("scaffoldingType")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("scaffoldingFullDimension")}>
                                {t("scaffoldingLogPositions:scaffoldingFullDimension")}{renderSortIndicator("scaffoldingFullDimension")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("fullWorkingTime")}>
                                {t("scaffoldingLogPositions:fullWorkingTime")}{renderSortIndicator("fullWorkingTime")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("technicalProtocolStatus")}>
                                {t("scaffoldingLogPositions:technicalProtocolStatus")}{renderSortIndicator("technicalProtocolStatus")}</Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("createdDatetime")}
                            >
                                {t("common:created")} {renderSortIndicator("createdDatetime")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("lastModifiedDatetime")}
                            >
                                {t("common:lastModified")} {renderSortIndicator("lastModifiedDatetime")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("edit")}</Table.ColumnHeader>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {positions.map((position) => {
                            const isExpanded = expandedRow === position.id;
                            const hasChildren = position.childPositions && position.childPositions.length > 0;
                            return (
                                <React.Fragment key={position.id}>
                                    <Table.Row key={position.id}
                                               bg={themeColors.bgColorSecondary}
                                               _hover={{
                                                   textDecoration: "none",
                                                   bg: themeColors.highlightBgColor,
                                                   color: themeColors.fontColorHover,
                                               }}
                                               onClick={() => toggleExpand(position.id!)}>

                                        <Table.Cell {...commonCellProps} width={"2%"}>{position.id}</Table.Cell>
                                        <Table.Cell {...commonCellProps}
                                                    width={"8%"}>
                                                    {hasChildren &&
                                                        <Badge colorPalette="blue" mr={1} size="xs">P</Badge>}
                                                        {position.scaffoldingNumber}</Table.Cell>
                                        <Table.Cell {...commonCellProps}
                                                    width={"20%"}>{position.assemblyLocation}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"}>
                                            {DateFormatter.formatDate(position.assemblyDate)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"}>
                                            {DateFormatter.formatDate(position.dismantlingDate)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"}>
                                            {DateFormatter.formatDate(position.dismantlingNotificationDate)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}
                                                    width={"5%"}>{t(`scaffoldingTypes:${position.scaffoldingType}`)}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"5%"}>
                                            {position.scaffoldingFullDimension} {position.scaffoldingFullDimensionUnit?.symbol}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"3%"}>
                                            {position.fullWorkingTime} r-h
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"8%"}>
                                            {t(`technicalProtocolStatuses:${position.technicalProtocolStatus}`)}
                                        </Table.Cell>
                                        <AuditCell
                                            value={position.createdDatetime}
                                            user={position.createdBy}
                                            cellProps={commonCellProps}
                                            width={"8%"}
                                        />
                                        <AuditCell
                                            value={position.lastModifiedDatetime}
                                            user={position.modifiedBy}
                                            cellProps={commonCellProps}
                                            width={"8%"}
                                        />
                                        <Table.Cell {...commonCellProps} onClick={(e) => e.stopPropagation()}>
                                            <HStack gap={1}>
                                                <AddScaffoldingLogPositionDialog
                                                    fetchPositions={fetchPositions}
                                                    scaffoldingLogId={position.scaffoldingLog?.id}
                                                    parentPosition={position.parentPosition ? position.parentPosition : position}
                                                    triggerLabel={t("addSubPosition", {ns: "scaffoldingLogPositions"})}
                                                    triggerIcon={<FaCirclePlus/>}
                                                    triggerColorPalette="orange"
                                                />
                                                <Button colorPalette="blue" size="2xs">
                                                    {t("edit", {ns: "scaffoldingLogPositions"})}
                                                    <FaPen/>
                                                </Button>
                                                <Button colorPalette="red" size="2xs"
                                                        onClick={() => handleDeleteClick(position.id!)}>
                                                    {t("delete", {ns: "common"})}
                                                    <FaExplosion/>
                                                    <FaRegTrashCan/>
                                                </Button>
                                            </HStack>
                                        </Table.Cell>
                                    </Table.Row>

                                    {isExpanded && (
                                        <Table.Row bg={themeColors.bgColorSecondary}>
                                            <Table.Cell colSpan={13} p={0}>
                                                <Collapsible.Root open={isExpanded}>
                                                    <Collapsible.Content>
                                                        <Box p={2} bg={themeColors.bgColorSecondary}
                                                             borderBottomWidth="1px"
                                                             borderColor={ "gray.400"}>
                                                            <HStack alignItems="start" gap={4} width="auto">

                                                                {/* TABELA 1: WYMIARY CZĄSTKOWE */}
                                                                <VStack align="start" flex={1} gap={1}>
                                                                    <Text fontWeight="bold" fontSize="sm"
                                                                          color={themeColors.fontColor}>
                                                                        {t("dimensionDescriptions:partialDimensions")}
                                                                    </Text>
                                                                    <Table.ScrollArea borderWidth="1px"
                                                                                      borderRadius="md"
                                                                                      borderColor="grey">
                                                                        <Table.Root size="sm" interactive
                                                                                    showColumnBorder>
                                                                            <Table.Header>
                                                                                <Table.Row
                                                                                    bg={themeColors.bgColorPrimary}>
                                                                                    <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:hshort")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:wshort")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:lshort")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:unitShort")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("scaffoldingTypes:scaffoldingTypeShort")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("scaffoldingOperationTypes:operation")}</Table.ColumnHeader>
                                                                                </Table.Row>
                                                                            </Table.Header>
                                                                            <Table.Body>
                                                                                {position.dimensions?.length > 0 ? (
                                                                                    position.dimensions.map((dim, idx) => (
                                                                                        <Table.Row key={dim.id || idx}
                                                                                                   _hover={{
                                                                                                       textDecoration: "none",
                                                                                                       bg: themeColors.highlightBgColor,
                                                                                                       color: themeColors.fontColorHover,
                                                                                                   }}>
                                                                                            <Table.Cell {...commonCellProps}>{dim.height} </Table.Cell>
                                                                                            <Table.Cell {...commonCellProps}>{dim.width} </Table.Cell>
                                                                                            <Table.Cell {...commonCellProps}>{dim.length}</Table.Cell>
                                                                                            <Table.Cell {...commonCellProps}> {dim.unit?.symbol}</Table.Cell>
                                                                                            <Table.Cell {...commonCellProps}><Badge
                                                                                                variant="outline"
                                                                                                size={"xs"}>{t(`dimensionTypes:${dim.dimensionType}`)}</Badge></Table.Cell>
                                                                                            <Table.Cell
                                                                                                fontSize="xs" {...commonCellProps}>{t(`scaffoldingOperationTypes:${dim.operationType}`)}</Table.Cell>
                                                                                        </Table.Row>
                                                                                    ))
                                                                                ) : (
                                                                                    <Table.Row>
                                                                                        <Table.Cell colSpan={5}
                                                                                                    textAlign="center"
                                                                                                    color={themeColors.fontColor}>
                                                                                            {t("dimensionDescriptions:noDimensions")}
                                                                                        </Table.Cell>
                                                                                    </Table.Row>
                                                                                )}
                                                                            </Table.Body>
                                                                        </Table.Root>
                                                                    </Table.ScrollArea>
                                                                </VStack>

                                                                {/* TABELA 2: CZAS PRACY */}
                                                                <VStack align="start" flex={2} gap={1}>
                                                                    <Text fontWeight="bold" fontSize="sm"
                                                                          color={themeColors.fontColor}>
                                                                        {t("workingTimes", {defaultValue: "Czas pracy"})}
                                                                    </Text>
                                                                    <Table.ScrollArea borderWidth="1px"
                                                                                      borderRadius="md"
                                                                                      borderColor="grey">
                                                                        <Table.Root size="sm" interactive
                                                                                    showColumnBorder>
                                                                            <Table.Header>
                                                                                <Table.Row
                                                                                    bg={themeColors.bgColorPrimary}>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>{t("employees:employees")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>{t("units:hours")}</Table.ColumnHeader>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>{t("scaffoldingOperationTypes:operation")}</Table.ColumnHeader>
                                                                                </Table.Row>
                                                                            </Table.Header>
                                                                            <Table.Body>
                                                                                {position.workingTimes?.length > 0 ? (
                                                                                    position.workingTimes.map((wt, idx) => (
                                                                                        <Table.Row key={wt.id || idx}
                                                                                                   _hover={{
                                                                                                       textDecoration: "none",
                                                                                                       bg: themeColors.highlightBgColor,
                                                                                                       color: themeColors.fontColorHover,
                                                                                                   }}>
                                                                                            <Table.Cell {...commonCellProps}>{wt.numberOfWorkers}</Table.Cell>
                                                                                            <Table.Cell {...commonCellProps}>{wt.numberOfHours} {wt.unit?.symbol}</Table.Cell>
                                                                                            <Table.Cell
                                                                                                fontSize="xs" {...commonCellProps}>{t(`scaffoldingOperationTypes:${wt.operationType}`)}</Table.Cell>
                                                                                        </Table.Row>
                                                                                    ))
                                                                                ) : (
                                                                                    <Table.Row>
                                                                                        <Table.Cell colSpan={3}
                                                                                                    textAlign="center"
                                                                                                    color="gray.500">
                                                                                            {t("noWorkingTime", {defaultValue: "Brak zapisów czasu pracy"})}
                                                                                        </Table.Cell>
                                                                                    </Table.Row>
                                                                                )}
                                                                            </Table.Body>
                                                                        </Table.Root>
                                                                    </Table.ScrollArea>
                                                                </VStack>

                                                                {/* TABELA 3: Zleceniodawca*/}
                                                                <VStack align="start" flex={11} gap={1}>
                                                                    <Text fontWeight="bold" fontSize="sm"
                                                                          color={themeColors.fontColor}>
                                                                        {t("scaffoldingLogPositions:contractor")}
                                                                    </Text>
                                                                    <Table.ScrollArea borderWidth="1px"
                                                                                      borderRadius="md"
                                                                                      borderColor="grey">
                                                                        <Table.Root size="sm" variant="outline"
                                                                                    interactive showColumnBorder>
                                                                            <Table.Header>
                                                                                <Table.Row
                                                                                    bg={themeColors.bgColorPrimary}>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                                                        {t("scaffoldingLogPositions:contractor")}
                                                                                    </Table.ColumnHeader>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                                                        {t("scaffoldingLogPositions:contractorContact")}
                                                                                    </Table.ColumnHeader>
                                                                                </Table.Row>
                                                                            </Table.Header>
                                                                            <Table.Body>
                                                                                <Table.Row _hover={{
                                                                                    textDecoration: "none",
                                                                                    bg: themeColors.highlightBgColor,
                                                                                    color: themeColors.fontColorHover,
                                                                                }}>
                                                                                    <Table.Cell {...commonCellProps}>
                                                                                        {position.contractor.name}
                                                                                    </Table.Cell>
                                                                                    <Table.Cell {...commonCellProps}>
                                                                                        {position.contractorContact.firstName} {position.contractorContact.lastName}
                                                                                    </Table.Cell>
                                                                                </Table.Row>
                                                                            </Table.Body>
                                                                        </Table.Root>
                                                                    </Table.ScrollArea>
                                                                </VStack>
                                                                {/* TABELA 4: Użytkownik*/}
                                                                <VStack align="start" flex={33} gap={1}>
                                                                    <Text fontWeight="bold" fontSize="sm"
                                                                          color={themeColors.fontColor}>
                                                                        {t("scaffoldingLogPositions:scaffoldingUser")}
                                                                    </Text>
                                                                    <Table.ScrollArea borderWidth="1px"
                                                                                      borderRadius="md"
                                                                                      borderColor="grey">
                                                                        <Table.Root size="sm" variant="outline"
                                                                                    interactive showColumnBorder>
                                                                            <Table.Header>
                                                                                <Table.Row
                                                                                    bg={themeColors.bgColorPrimary}>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                                                        {t("scaffoldingLogPositions:scaffoldingUser")}
                                                                                    </Table.ColumnHeader>
                                                                                    <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                                                        {t("scaffoldingLogPositions:scaffoldingUserContact")}
                                                                                    </Table.ColumnHeader>
                                                                                </Table.Row>
                                                                            </Table.Header>
                                                                            <Table.Body>
                                                                                <Table.Row _hover={{
                                                                                    textDecoration: "none",
                                                                                    bg: themeColors.highlightBgColor,
                                                                                    color: themeColors.fontColorHover,
                                                                                }}>
                                                                                    <Table.Cell {...commonCellProps}>
                                                                                        {position.scaffoldingUser.name}
                                                                                    </Table.Cell>
                                                                                    <Table.Cell {...commonCellProps}>
                                                                                        {position.scaffoldingUserContact.firstName} {position.scaffoldingUserContact.lastName}
                                                                                    </Table.Cell>
                                                                                </Table.Row>
                                                                            </Table.Body>
                                                                        </Table.Root>
                                                                    </Table.ScrollArea>
                                                                </VStack>
                                                            </HStack>
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

export default ScaffoldingLogPositionTable;