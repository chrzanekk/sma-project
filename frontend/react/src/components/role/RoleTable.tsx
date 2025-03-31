import React, { useState } from "react";
import { Button, Table, Text, useDisclosure } from "@chakra-ui/react";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import { RoleDTO } from "@/types/role-types.ts";
import { useTranslation } from "react-i18next";
import DateFormatter from "@/utils/date-formatter.ts";
import { Field } from "@/components/ui/field.tsx";
import { useThemeColors } from "@/theme/theme-colors";
import { useTableStyles } from "@/components/shared/tableStyles";

interface Props {
    roles: RoleDTO[];
    onDelete: (id: number) => void;
}

const RoleTable: React.FC<Props> = ({ roles, onDelete }) => {
    const { t } = useTranslation("auth");
    const { open, onOpen, onClose } = useDisclosure();
    const [selectedRoleId, setSelectedRoleId] = useState<number | null>(null);
    const themeColors = useThemeColors();
    const { commonCellProps, commonColumnHeaderProps } = useTableStyles();

    const handleDeleteClick = (id: number) => {
        setSelectedRoleId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedRoleId !== null) {
            onDelete(selectedRoleId);
        }
        onClose();
    };

    if (!roles || roles.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound", { ns: "common" })}</Text>
            </Field>
        );
    }

    return (
        <>
            <Table.ScrollArea height="auto" borderWidth="1px" borderRadius="md" borderColor="grey">
                <Table.Root size="sm" interactive showColumnBorder color={themeColors.fontColor}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>ID</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>
                                {t("shared.roleName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>
                                {t("createDate", { ns: "common" })}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>
                                {t("lastModifiedDate", { ns: "common" })}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>
                                {t("delete", { ns: "common" })}
                            </Table.ColumnHeader>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {roles.map((role) => (
                            <Table.Row
                                key={role.id}
                                bg={themeColors.bgColorSecondary}
                                _hover={{
                                    textDecoration: "none",
                                    bg: themeColors.highlightBgColor,
                                    color: themeColors.fontColorHover,
                                }}
                            >
                                <Table.Cell {...commonCellProps}>{role.id}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{role.name}</Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {DateFormatter.formatDateTime(role.createdDatetime!)}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {DateFormatter.formatDateTime(role.lastModifiedDatetime!)}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <Button colorPalette="red" size="2xs" onClick={() => handleDeleteClick(role.id!)}>
                                        {t("delete", { ns: "common" })}
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
                title={t("deleteConfirmation.title", { ns: "common" })}
                message={t("deleteConfirmation.message", { ns: "common" })}
                confirmText={t("delete", { ns: "common" })}
                cancelText={t("cancel", { ns: "common" })}
            />
        </>
    );
};

export default RoleTable;
