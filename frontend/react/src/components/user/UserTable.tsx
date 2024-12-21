import React, {useState} from 'react';
import classNames from 'classnames';
import {Button, HStack, Table, Tbody, Td, Text, Th, Thead, Tr, useDisclosure} from '@chakra-ui/react';
import {UserDTO} from "@/types/user-types.ts";
import '@/theme/css/global-table-styles.css';
import '@/theme/css/user-table-styles.css';
import {useTranslation} from "react-i18next";
import EditUserDataDrawer from "@/components/user/EditUserDataDrawer.tsx";
import {useAuth} from "@/context/AuthContext.tsx";
import EditUserPasswordDrawer from "@/components/user/EditUserPasswordDrawer.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import EditUserRolesDrawer from "@/components/user/EditUserRolesDrawer.tsx";

const tableClass = classNames('custom-table', 'user-table');

interface Props {
    users: UserDTO[];
    onDelete: (id: number) => void;
    fetchUsers: () => void;
}

const UserTable: React.FC<Props> = ({users, onDelete, fetchUsers}) => {
    const {t} = useTranslation('auth')
    const {user: currentUser} = useAuth();
    const {isOpen, onOpen, onClose} = useDisclosure();
    const [selectedUserId, setSelectedUserId] = useState<number | null>(null);

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
        return <Text fontSize={20} align={"center"}>{t('dataNotFound', {ns: "common"})}</Text>
    }

    return (
        <>
            <Table className={tableClass}>
                <Thead>
                    <Tr>
                        <Th>ID</Th>
                        <Th>{t('shared.email')}</Th>
                        <Th>{t('shared.login')}</Th>
                        <Th>{t('shared.firstName')}</Th>
                        <Th>{t('shared.lastName')}</Th>
                        <Th>{t('shared.position')}</Th>
                        <Th>{t('shared.roles')}</Th>
                        <Th>{t('shared.locked')}</Th>
                        <Th>{t('shared.enabled')}</Th>
                        <Th>{t('edit', {ns: "common"})}</Th>
                        <Th>{t('delete', {ns: "common"})}</Th>
                    </Tr>
                </Thead>
                <Tbody>
                    {users.map((user) => (
                        <Tr key={user.id}>
                            <Td>{user.id}</Td>
                            <Td>{user.email}</Td>
                            <Td>{user.login}</Td>
                            <Td>{user.firstName}</Td>
                            <Td>{user.lastName}</Td>
                            <Td>{user.position}</Td>
                            <Td>
                                <HStack spacing={2} wrap="wrap">
                                    {user.roles?.map((role, index) => (
                                        <Button
                                            key={index}
                                            size="xs"
                                            fontSize={"xs"}
                                            variant="solid"
                                            colorScheme="teal"
                                            borderRadius="md"
                                            textTransform="uppercase"
                                        >
                                            {role.name.replace("ROLE_", "")}
                                        </Button>
                                    ))}
                                </HStack>
                            </Td>
                            <Td>{user.locked ? t('yes', {ns: "common"}) : t('no', {ns: "common"})}</Td>
                            <Td>{user.enabled ? t('yes', {ns: "common"}) : t('no', {ns: "common"})}</Td>
                            <Td>
                                <HStack spacing={2} alignContent={"center"}>
                                    <EditUserDataDrawer
                                        fetchUsers={fetchUsers}
                                        userId={user.id!}/>
                                    <EditUserPasswordDrawer
                                        fetchUsers={fetchUsers}
                                        userId={user.id!}
                                        currentUserId={currentUser?.id!}
                                        login={user.login}/>
                                    <EditUserRolesDrawer
                                        fetchUsers={fetchUsers}
                                        userId={user.id!}
                                        currentUserId={currentUser?.id!}
                                        login={user.login}/>
                                </HStack>
                            </Td>
                            <Td>
                                <Button
                                    colorScheme="red"
                                    size={"xs"}
                                    onClick={() => handleDeleteClick(user.id!)}
                                    isDisabled={currentUser?.id === user.id}>
                                    {t('delete', {ns: "common"})}
                                </Button>
                            </Td>
                        </Tr>
                    ))}
                </Tbody>
            </Table>

            <ConfirmModal
                isOpen={isOpen}
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
