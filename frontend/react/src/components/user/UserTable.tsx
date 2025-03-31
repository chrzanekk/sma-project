import React, {useState} from "react";
import {Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {UserDTO} from "@/types/user-types.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {useTranslation} from "react-i18next";
import DateFormatter from "@/utils/date-formatter.ts";
import {Field} from "@/components/ui/field.tsx";
import EditUserDataDrawer from "@/components/user/EditUserDataDrawer.tsx";
import EditUserPasswordDrawer from "@/components/user/EditUserPasswordDrawer.tsx";
import EditUserRolesDrawer from "@/components/user/EditUserRolesDrawer.tsx";
import {useThemeColors} from "@/theme/theme-colors";
import {useTableStyles} from "@/components/shared/tableStyles";
import {useAuth} from "@/context/AuthContext.tsx";

interface Props {
    users: UserDTO[];
    onDelete: (id: number) => void;
    fetchUsers: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
}

const UserTable: React.FC<Props> = ({users, onDelete, fetchUsers, onSortChange, sortField, sortDirection}) => {
    const {t} = useTranslation("auth");
    const {user: currentUser} = useAuth();
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    const handleDeleteClick = (id: number) => {
        setSelectedUserId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedUserId !== null) {
            onDelete(selectedUserId);
        }
        onClose();
    };

    const renderSortIndicator = (field: string) => {

        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!users || users.length === 0) {
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
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("id")}
                            >ID {renderSortIndicator("id")}</Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("email")}
                            >{t("shared.email")}{renderSortIndicator("email")}</Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("login")}
                            >{t("shared.login")}{renderSortIndicator("login")}</Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("firstName")}
                            >{t("shared.firstName")}{renderSortIndicator("firstName")}</Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("lastName")}
                            >{t("shared.lastName")}{renderSortIndicator("lastName")}</Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("position")}
                            >{t("shared.position")}{renderSortIndicator("position")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("shared.roles")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("shared.locked")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("shared.enabled")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("createDate", {ns: "common"})}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("lastModifiedDate", {ns: "common"})}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("edit", {ns: "common"})}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("delete", {ns: "common"})}</Table.ColumnHeader>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {users.map((user) => (
                            <Table.Row
                                key={user.id}
                                bg={themeColors.bgColorSecondary}
                                _hover={{
                                    textDecoration: "none",
                                    bg: themeColors.highlightBgColor,
                                    color: themeColors.fontColorHover,
                                }}
                            >
                                <Table.Cell {...commonCellProps}>{user.id}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{user.email}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{user.login}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{user.firstName}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{user.lastName}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{user.position}</Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <HStack gap={1} wrap="wrap">
                                        {user.roles?.map((role, index) => (
                                            <Button
                                                key={index}
                                                size="2xs"
                                                fontSize="x-small"
                                                variant="solid"
                                                colorPalette="teal"
                                                borderRadius="md"
                                                textTransform="uppercase"
                                            >
                                                {role.name.replace("ROLE_", "")}
                                            </Button>
                                        ))}
                                    </HStack>
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {user.locked ? t("yes", {ns: "common"}) : t("no", {ns: "common"})}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {user.enabled ? t("yes", {ns: "common"}) : t("no", {ns: "common"})}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {DateFormatter.formatDateTime(user.createdDatetime!)}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    {DateFormatter.formatDateTime(user.lastModifiedDatetime!)}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <HStack gap={1} alignContent="center">
                                        <EditUserDataDrawer fetchUsers={fetchUsers} userId={user.id!}/>
                                        <EditUserPasswordDrawer
                                            fetchUsers={fetchUsers}
                                            userId={user.id!}
                                            currentUserId={currentUser?.id}
                                            login={user.login}
                                        />
                                        <EditUserRolesDrawer
                                            fetchUsers={fetchUsers}
                                            userId={user.id!}
                                            currentUserId={currentUser?.id}
                                            login={user.login}
                                        />
                                    </HStack>
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <Button
                                        colorPalette="red"
                                        size="2xs"
                                        onClick={() => handleDeleteClick(user.id!)}
                                        disabled={currentUser?.id === user.id}
                                    >
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
    );
};

export default UserTable;
