import React from 'react';
import { Table, Thead, Tbody, Tr, Th, Td, Button } from '@chakra-ui/react';
import {UserDTO} from "@/types/user-types.ts";

interface Props {
    users: UserDTO[];
    onDelete: (id: number) => void;
}

const UserTable: React.FC<Props> = ({ users, onDelete }) => {
    if(!users || users.length === 0) {
        return <div>No users found.</div>
    }

    return (
        <Table variant="striped" colorScheme="gray" size={"xs"}>
            <Thead>
                <Tr sx={{ fontSize: "12px", height: "30px" }}>
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
                        <Td>{user.email}</Td>
                        <Td>{user.login}</Td>
                        <Td>{user.firstName}</Td>
                        <Td>{user.lastName}</Td>
                        <Td>{user.position}</Td>
                        <Td>{user.locked ? 'Yes' : 'No'}</Td>
                        <Td>{user.enabled ? 'Yes' : 'No'}</Td>
                        <Td>
                            <Button colorScheme="red" onClick={() => onDelete(user.id!)} size={"sm"}>
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
