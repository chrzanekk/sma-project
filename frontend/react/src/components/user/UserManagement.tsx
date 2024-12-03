import React, {useEffect, useState} from 'react';
import {Box, Button} from '@chakra-ui/react';
import UserFilterForm from './UserFilterForm';
import UserTable from './UserTable';
import {UserDTO} from "@/types/user-types.ts";
import {deleteUserById, getUsersByFilter} from "@/services/user-service.ts";


const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<UserDTO[]>([]);

    const fetchUsers = async (filter = {}) => {
        try {
            const users = await getUsersByFilter(filter);
            setUsers(users);
        } catch (err) {
            console.error('Error fetching users: ', err);
        }
    };

    const handleDelete = async (id: number) => {
        await deleteUserById(id);
        fetchUsers().then(() => {
            console.log('User fetched successfully')
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        fetchUsers(values).then(() => {
            console.log('User fetched successfully')
        });
    };

    useEffect(() => {
        fetchUsers().then(() => {
            console.log('User fetched successfully')
        });
    }, []);

    return (
        <Box>
            <Button colorScheme="green" mb={4}>
                Add User
            </Button>
            <UserFilterForm onSubmit={handleFilterSubmit}/>
            <UserTable users={users} onDelete={handleDelete}/>
        </Box>
    );
};

export default UserManagement;
