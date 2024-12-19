import React, {useCallback, useEffect, useState} from 'react';
import {Flex} from '@chakra-ui/react';
import UserFilterForm from './UserFilterForm';
import UserTable from './UserTable';
import {UserDTO} from "@/types/user-types.ts";
import {deleteUserById, getUsersByFilter} from "@/services/user-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import UserLayout from "@/components/user/UserLayout.tsx";
import AddUserDrawer from "@/components/user/AddUserDrawer.tsx";


const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const fetchUsers = useCallback(async (filter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getUsersByFilter({...filter, page, size});
            setUsers(response.users);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching users: ', err);
        }
    }, [rowsPerPage]);

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchUsers({}, 0, size).catch(() => {});
    };

    const handleDelete = async (id: number) => {
        await deleteUserById(id);
        fetchUsers().catch(() => {});
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        fetchUsers(values, 0, rowsPerPage).catch(() => {});
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchUsers({}, page, rowsPerPage).catch(() => {});
    };

    useEffect(() => {
        fetchUsers({}, currentPage).then(() => {
            console.log('User fetched successfully')
        });
    }, [fetchUsers, currentPage]);

    return (
        <UserLayout
            filters={<UserFilterForm onSubmit={handleFilterSubmit}/>}
            addUserButton={<Flex justifyContent={"center"}>
                <AddUserDrawer fetchUsers={fetchUsers}/>
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
