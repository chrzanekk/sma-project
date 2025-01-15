import React, {useState} from 'react';
import classNames from 'classnames';
import {Button, HStack, Table, Text, useDisclosure} from '@chakra-ui/react';
import {UserDTO} from "@/types/user-types.ts";
import '@/theme/css/global-table-styles.css';
import '@/theme/css/user-table-styles.css';
import {useTranslation} from "react-i18next";
import EditUserDataDrawer from "@/components/user/EditUserDataDrawer.tsx";
import {useAuth} from "@/context/AuthContext.tsx";
import EditUserPasswordDrawer from "@/components/user/EditUserPasswordDrawer.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import EditUserRolesDrawer from "@/components/user/EditUserRolesDrawer.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import {Field} from "@/components/ui/field.tsx";
import {useTheme} from "next-themes";


interface Props {
    users: UserDTO[];
    onDelete: (id: number) => void;
    fetchUsers: () => void;
}

const UserTable: React.FC<Props> = ({users, onDelete, fetchUsers}) => {
    const {t} = useTranslation('auth')
    const {user: currentUser} = useAuth();
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
    const {theme} = useTheme();

    const tableClass = classNames('custom-table', 'user-table', {
        'dark-theme': theme === 'dark'
    });

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

    if (!users || users.length === 0) {
        return (
            <Field alignContent={"center"}>
                <Text fontSize={20}>{t('dataNotFound', {ns: "common"})}</Text>
            </Field>)
    }

    return (
        <>
            <Table.Root className={tableClass}>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeader>ID</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.email')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.login')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.firstName')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.lastName')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.position')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.roles')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.locked')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.enabled')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('createDate', {ns: "common"})}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('lastModifiedDate', {ns: "common"})}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('edit', {ns: "common"})}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('delete', {ns: "common"})}</Table.ColumnHeader>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {users.map((user) => (
                        <Table.Row key={user.id}>
                            <Table.Cell>{user.id}</Table.Cell>
                            <Table.Cell>{user.email}</Table.Cell>
                            <Table.Cell>{user.login}</Table.Cell>
                            <Table.Cell>{user.firstName}</Table.Cell>
                            <Table.Cell>{user.lastName}</Table.Cell>
                            <Table.Cell>{user.position}</Table.Cell>
                            <Table.Cell>
                                <HStack gap={1} wrap="wrap">
                                    {user.roles?.map((role, index) => (
                                        <Button
                                            key={index}
                                            size={"2xs"}
                                            fontSize={"x-small"}
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
                            <Table.Cell>{user.locked ? t('yes', {ns: "common"}) : t('no', {ns: "common"})}</Table.Cell>
                            <Table.Cell>{user.enabled ? t('yes', {ns: "common"}) : t('no', {ns: "common"})}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(user.createdDatetime!)}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(user.lastModifiedDatetime!)}</Table.Cell>
                            <Table.Cell>
                                <HStack gap={1} alignContent={"center"}>
                                    <EditUserDataDrawer
                                        fetchUsers={fetchUsers}
                                        userId={user.id!}/>
                                    <EditUserPasswordDrawer
                                        fetchUsers={fetchUsers}
                                        userId={user.id!}
                                        currentUserId={currentUser?.id}
                                        login={user.login}/>
                                    <EditUserRolesDrawer
                                        fetchUsers={fetchUsers}
                                        userId={user.id!}
                                        currentUserId={currentUser?.id}
                                        login={user.login}/>
                                </HStack>
                            </Table.Cell>
                            <Table.Cell>
                                <Button
                                    colorPalette="red"
                                    size={"xs"}
                                    onClick={() => handleDeleteClick(user.id!)}
                                    disabled={currentUser?.id === user.id}>
                                    {t('delete', {ns: "common"})}
                                </Button>
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
        </>
    );
};

export default UserTable;
