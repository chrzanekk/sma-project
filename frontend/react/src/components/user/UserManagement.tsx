import React, {useEffect, useState} from 'react';
import {Button, Flex} from '@chakra-ui/react';
import UserFilterForm from './UserFilterForm';
import UserTable from './UserTable';
import {UserDTO} from "@/types/user-types.ts";
import {deleteUserById, getUsersByFilter} from "@/services/user-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import UserLayout from "@/components/user/UserLayout.tsx";


const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);


    const fetchUsers = async (filter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getUsersByFilter({...filter, page, size});
            setUsers(response.users);
            setTotalPages(response.totalPages)
        } catch (err) {
            console.error('Error fetching users: ', err);
        }
    };

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchUsers({}, 0, size).then(() => {
            console.log('User fetched successfully')
        });
    };

    const handleDelete = async (id: number) => {
        await deleteUserById(id);
        fetchUsers().then(() => {
            console.log('User fetched successfully')
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        fetchUsers(values, 0, rowsPerPage).then(() => {
            console.log('User fetched successfully')
        });
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchUsers({}, page, rowsPerPage).then(() => {
            console.log('User fetched successfully')
        });
    };

    useEffect(() => {
        fetchUsers({}, currentPage).then(() => {
            console.log('User fetched successfully')
        });
    }, [currentPage]);

    return (
        <UserLayout
            filters={<UserFilterForm onSubmit={handleFilterSubmit}/>}
            addUserButton={<Flex justifyContent={"center"}>
                <Button colorScheme="green"
                        onClick={() => console.log("Add User")}
                        size={"sm"}
                        p={1}>
                    Add User
                </Button>
            </Flex>

            }
            table={<UserTable users={users} onDelete={handleDelete}/>}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handlePageChange}
                    onRowsPerPageChange={handleRowsPerPageChange}
                />
            }
        />
    );
};

export default UserManagement;
