import {FetchablePositionDTO, PositionBaseDTO, PositionDTO} from "@/types/position-types.ts";
import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import AuditCell from "@/components/shared/AuditCell.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import EditPositionDrawer from "@/components/position/EditPositionDrawer.tsx";

function isFetchablePosition(
    position: PositionBaseDTO
): position is FetchablePositionDTO {
    return "createdDatetime" in position;
}

interface Props<T extends PositionBaseDTO> {
    positions: T[];
    onDelete: (id: number) => void;
    fetchPositions: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean;
}

const GenericPositionTable = <T extends PositionDTO>({
                                                         positions,
                                                         fetchPositions,
                                                         onDelete,
                                                         onSortChange,
                                                         sortField,
                                                         sortDirection,
                                                         extended = false,
                                                     }: Props<T>) => {
    const {t} = useTranslation(["common", "positions"]);
    const {open, onOpen, onClose} = useDisclosure();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [selectedPositionId, setSelectedPositionId] = useState<number | null>(null);
    const themeColors = useThemeColors();

    const handleDeleteClick = (id: number) => {
        setSelectedPositionId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedPositionId !== null) {
            onDelete(selectedPositionId);
        }
        onClose();
    };

    const renderSortIndicator = (field: string) => {

        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!positions || positions.length === 0) {
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
                                onClick={() => onSortChange("name")}
                            >
                                {t("positions:name")} {renderSortIndicator("name")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("description")}
                            >
                                {t("positions:description")} {renderSortIndicator("description")}
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
                        {positions.map((position) => (
                            <Table.Row key={position.id}
                                       bg={themeColors.bgColorSecondary}
                                       _hover={{
                                           textDecoration: 'none',
                                           bg: themeColors.highlightBgColor,
                                           color: themeColors.fontColorHover
                                       }}
                            >
                                <Table.Cell {...commonCellProps} width={"2%"}>{position.id}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"30%"}>{position.name}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"30%"}>{position.description}</Table.Cell>
                                {extended && isFetchablePosition(position) && (
                                    <>
                                        <AuditCell
                                            value={position.createdDatetime}
                                            user={position.createdBy}
                                            cellProps={commonCellProps}
                                            width={"15%"}
                                        />
                                        <AuditCell
                                            value={position.lastModifiedDatetime}
                                            user={position.modifiedBy}
                                            cellProps={commonCellProps}
                                            width={"15%"}
                                        />
                                    </>
                                )}
                                {extended && (
                                    <Table.Cell {...commonCellProps}>
                                        <HStack gap={1} justifyContent="center">
                                            <EditPositionDrawer
                                                fetchPositions={fetchPositions}
                                                positionId={position.id!}
                                            />
                                            <Button
                                                colorPalette="orange"
                                                size="2xs"
                                                onClick={() => handleDeleteClick(position.id!)}
                                            >
                                                {t("delete", {ns: "common"})}
                                            </Button>
                                        </HStack>
                                    </Table.Cell>
                                )}
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

export default GenericPositionTable;
