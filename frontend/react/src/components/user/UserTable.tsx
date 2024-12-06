import React from 'react';
import classNames from 'classnames';
import {Button, Table, Tbody, Td, Th, Thead, Tr} from '@chakra-ui/react';
import {UserDTO} from "@/types/user-types.ts";
import '@/theme/css/global-table-styles.css';
import '@/theme/css/user-table-styles.css';

const tableClass = classNames('custom-table', 'user-table');

interface Props {
    users: UserDTO[];
    onDelete: (id: number) => void;
}

const UserTable: React.FC<Props> = ({users, onDelete}) => {
    if (!users || users.length === 0) {
        return <div>No users found.</div>
    }

    return (
        <Table className={tableClass}>
            <Thead>
                <Tr>
                    <Th>ID</Th>
                    <Th>Email</Th>
                    <Th>Login</Th>
                    <Th>First Name</Th>
                    <Th>Last Name</Th>
                    <Th>Position</Th>
                    <Th>Locked</Th>
                    <Th>Enabled</Th>
                    <Th>Actions</Th>
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
                        <Td>{user.locked ? 'Yes' : 'No'}</Td>
                        <Td>{user.enabled ? 'Yes' : 'No'}</Td>
                        <Td>
                            <Button colorScheme="red" onClick={() => onDelete(user.id!)} size={"xs"}>
                                Delete
                            </Button>
                        </Td>
                    </Tr>
                ))}
            </Tbody>
        </Table>
    );
};

export default UserTable;
