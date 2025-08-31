import React from 'react';
import {Flex} from '@chakra-ui/react';
import UserFilterForm from './UserFilterForm';
import {UserDTO} from "@/types/user-types.ts";
import {deleteUserById, getUsersByFilter} from "@/services/user-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import UserLayout from "@/components/user/UserLayout.tsx";
import AddUserDrawer from "@/components/user/AddUserDrawer.tsx";
import UserTable from "@/components/user/UserTable.tsx";
import {useDataManagement} from "@/hooks/useDataManagement.ts";

const UserManagement: React.FC = () => {
    const {
        items: users,
        currentPage,
        totalPages,
        rowsPerPage,
        sortField,
        sortDirection,
        handlers: {
            onPageChange,
            onRowsPerPageChange,
            onSortChange,
            onFilterSubmit,
            onDelete
        }
    } = useDataManagement<UserDTO>(
        // funkcja pobierająca dane
        async (params) => {
            const response = await getUsersByFilter(params);
            return {
                data: response.users,
                totalPages: response.totalPages
            };
        },
        deleteUserById,
        {},
        false
    );

    return (
        <UserLayout
            filters={<UserFilterForm onSubmit={onFilterSubmit}/>}
            addUserButton={
                <Flex justifyContent={"center"}>
                    <AddUserDrawer fetchUsers={() => onFilterSubmit({})}/>
                </Flex>
            }
            table={<UserTable
                users={users}
                onDelete={onDelete}
                fetchUsers={() => onFilterSubmit({})}
                onSortChange={onSortChange}
                sortField={sortField}
                sortDirection={sortDirection}
            />}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={onPageChange}
                    onRowsPerPageChange={onRowsPerPageChange}
                />
            }
        />
    );
};

export default UserManagement;
