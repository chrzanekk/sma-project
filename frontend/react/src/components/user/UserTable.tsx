import React from 'react';
import classNames from 'classnames';
import {Button, Table, Tbody, Td, Text, Th, Thead, Tr} from '@chakra-ui/react';
import {UserDTO} from "@/types/user-types.ts";
import '@/theme/css/global-table-styles.css';
import '@/theme/css/user-table-styles.css';
import {useTranslation} from "react-i18next";

const tableClass = classNames('custom-table', 'user-table');

interface Props {
    users: UserDTO[];
    onDelete: (id: number) => void;
}

const UserTable: React.FC<Props> = ({users, onDelete}) => {
    const {t} = useTranslation('auth')

    if (!users || users.length === 0) {
        return <Text fontSize={20} align={"center"}>{t('dataNotFound', {ns: "common"})}</Text>
    }

    return (
        <Table className={tableClass}>
            <Thead>
                <Tr>
                    <Th>ID</Th>
                    <Th>{t('shared.email')}</Th>
                    <Th>{t('shared.login')}</Th>
                    <Th>{t('shared.firstName')}</Th>
                    <Th>{t('shared.lastName')}</Th>
                    <Th>{t('shared.position')}</Th>
                    <Th>{t('shared.locked')}</Th>
                    <Th>{t('shared.enabled')}</Th>
                    <Th>{t('actions', {ns: "common"})}</Th>
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
                        <Td>{user.locked ? t('yes', {ns: "common"}) : t('no', {ns: "common"})}</Td>
                        <Td>{user.enabled ? t('yes', {ns: "common"}) : t('no', {ns: "common"})}</Td>
                        <Td>
                            <Button colorScheme="red" onClick={() => onDelete(user.id!)} size={"xs"}>
                                {t('delete', {ns: "common"})}
                            </Button>
                        </Td>
                    </Tr>
                ))}
            </Tbody>
        </Table>
    );
};

export default UserTable;
