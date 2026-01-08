import {FetchableScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";
import {useTranslation} from "react-i18next";
import {
    Badge,
    Box,
    Button,
    Collapsible,
    HStack,
    Table,
    Text,
    useDisclosure,
    VStack
} from "@chakra-ui/react";
import React, {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import { useTableStyles } from "@/components/shared/tableStyles.ts";
import AuditCell from "@/components/shared/AuditCell.tsx";

interface ScaffoldingLogPositionTableProps {
    positions: FetchableScaffoldingLogPositionDTO[];
    onDelete: (id: number) => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
}

const ScaffoldingLogPositionTable: React.FC<ScaffoldingLogPositionTableProps> = ({
                                                                                     positions,
                                                                                     onDelete,
                                                                                     onSortChange,
                                                                                     sortField,
                                                                                     sortDirection
                                                                                 }) => {
    const {t} = useTranslation(['scaffoldingLogPositions', 'common']);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedId, setSelectedId] = useState<number | null>(null);
    const [expandedRow, setExpandedRow] = useState<number | null>(null);

    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    const toggleExpand = (id: number) => {
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
                                {t("scaffoldingLogPositions:dismantlingNotificationDate")}{renderSortIndicator("dismantlingNotificationDate")}</Table.ColumnHeader>
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
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("contractor")}>
                                {t("scaffoldingLogPositions:contractor")}{renderSortIndicator("contractor")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("contractorContact")}>
                                {t("scaffoldingLogPositions:contractorContact")}{renderSortIndicator("contractorContact")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("scaffoldingUser")}>
                                {t("scaffoldingLogPositions:scaffoldingUser")}{renderSortIndicator("scaffoldingUser")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("scaffoldingUserContact")}>
                                {t("scaffoldingLogPositions:scaffoldingUserContact")}{renderSortIndicator("scaffoldingUserContact")}</Table.ColumnHeader>
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

                                        <Table.Cell {...commonCellProps}>{position.id}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{position.scaffoldingNumber}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{position.assemblyLocation}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {DateFormatter.formatDateTime(position.assemblyDate)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {DateFormatter.formatDateTime(position.dismantlingDate)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {DateFormatter.formatDateTime(position.dismantlingNotificationDate)}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>{position.scaffoldingType}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.scaffoldingFullDimension} {position.scaffoldingFullDimensionUnit?.symbol}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.fullWorkingTime} h
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.technicalProtocolStatus}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.contractor.name}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.contractorContact.firstName} {position.contractorContact.lastName}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.scaffoldingUser.name}
                                        </Table.Cell>
                                        <Table.Cell {...commonCellProps}>
                                            {position.scaffoldingUserContact.firstName} {position.scaffoldingUserContact.lastName}
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
                                            <Button colorPalette="red" size="2xs"
                                                    onClick={() => handleDeleteClick(position.id!)}>
                                                {t("delete", {ns: "common"})}
                                            </Button>
                                        </Table.Cell>
                                    </Table.Row>


                                    {isExpanded && (
                                        <Table.Row bg={themeColors.bgColorSecondary}>
                                            <Table.Cell colSpan={9} p={0}>
                                                <Collapsible.Root open={isExpanded}>
                                                    <Collapsible.Content>
                                                        <Box p={4} bg="gray.50" borderBottomWidth="1px"
                                                             borderColor="gray.200">
                                                            <HStack alignItems="start" gap={6} width="100%">

                                                                {/* TABELA 1: WYMIARY CZĄSTKOWE */}
                                                                <VStack align="start" flex={1} gap={2}>
                                                                    <Text fontWeight="bold" fontSize="sm"
                                                                          color="gray.600">
                                                                        {t("partialDimensions", {defaultValue: "Wymiary cząstkowe"})}
                                                                    </Text>
                                                                    <Table.Root size="sm" variant="outline" bg="white">
                                                                        <Table.Header>
                                                                            <Table.Row>
                                                                                <Table.ColumnHeader>Wys.</Table.ColumnHeader>
                                                                                <Table.ColumnHeader>Szer.</Table.ColumnHeader>
                                                                                <Table.ColumnHeader>Dł.</Table.ColumnHeader>
                                                                                <Table.ColumnHeader>Typ</Table.ColumnHeader>
                                                                                <Table.ColumnHeader>Operacja</Table.ColumnHeader>
                                                                            </Table.Row>
                                                                        </Table.Header>
                                                                        <Table.Body>
                                                                            {position.dimensions?.length > 0 ? (
                                                                                position.dimensions.map((dim, idx) => (
                                                                                    <Table.Row key={dim.id || idx}>
                                                                                        <Table.Cell>{dim.height} {dim.unit?.symbol}</Table.Cell>
                                                                                        <Table.Cell>{dim.width} {dim.unit?.symbol}</Table.Cell>
                                                                                        <Table.Cell>{dim.length} {dim.unit?.symbol}</Table.Cell>
                                                                                        <Table.Cell><Badge
                                                                                            variant="outline">{dim.dimensionType}</Badge></Table.Cell>
                                                                                        <Table.Cell
                                                                                            fontSize="xs">{dim.operationType}</Table.Cell>
                                                                                    </Table.Row>
                                                                                ))
                                                                            ) : (
                                                                                <Table.Row>
                                                                                    <Table.Cell colSpan={5}
                                                                                                textAlign="center"
                                                                                                color="gray.500">
                                                                                        {t("noDimensions", {defaultValue: "Brak wymiarów"})}
                                                                                    </Table.Cell>
                                                                                </Table.Row>
                                                                            )}
                                                                        </Table.Body>
                                                                    </Table.Root>
                                                                </VStack>

                                                                {/* TABELA 2: CZAS PRACY */}
                                                                <VStack align="start" flex={1} gap={2}>
                                                                    <Text fontWeight="bold" fontSize="sm"
                                                                          color="gray.600">
                                                                        {t("workingTimes", {defaultValue: "Czas pracy"})}
                                                                    </Text>
                                                                    <Table.Root size="sm" variant="outline" bg="white">
                                                                        <Table.Header>
                                                                            <Table.Row>
                                                                                <Table.ColumnHeader>Pracownicy</Table.ColumnHeader>
                                                                                <Table.ColumnHeader>Godziny</Table.ColumnHeader>
                                                                                <Table.ColumnHeader>Operacja</Table.ColumnHeader>
                                                                            </Table.Row>
                                                                        </Table.Header>
                                                                        <Table.Body>
                                                                            {position.workingTimes?.length > 0 ? (
                                                                                position.workingTimes.map((wt, idx) => (
                                                                                    <Table.Row key={wt.id || idx}>
                                                                                        <Table.Cell>{wt.numberOfWorkers}</Table.Cell>
                                                                                        <Table.Cell>{wt.numberOfHours} {wt.unit?.symbol}</Table.Cell>
                                                                                        <Table.Cell
                                                                                            fontSize="xs">{wt.operationType}</Table.Cell>
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