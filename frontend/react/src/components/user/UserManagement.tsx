import React, {useCallback, useEffect, useState} from 'react';
import {Flex} from '@chakra-ui/react';
import UserFilterForm from './UserFilterForm';
import {UserDTO} from "@/types/user-types.ts";
import {deleteUserById, getUsersByFilter} from "@/services/user-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import UserLayout from "@/components/user/UserLayout.tsx";
import AddUserDrawer from "@/components/user/AddUserDrawer.tsx";
import UserTable from "@/components/user/UserTable.tsx";


const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});

    const fetchUsers = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getUsersByFilter({
                ...customFilter,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setUsers(response.users);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching users: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection]);

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchUsers({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        await deleteUserById(id);
        fetchUsers({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }).catch(() => {
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        setFilter(values);
        fetchUsers({
            ...values,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchUsers({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, page, rowsPerPage).catch(() => {
        });
    };

    const handleSortChange = (field: string) => {
        let newDirection: "asc" | "desc" = "asc";
        if (sortField === field) {
            newDirection = sortDirection === "asc" ? "desc" : "asc";
        }
        setSortField(field);
        setSortDirection(newDirection);
        fetchUsers({...filter, sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        fetchUsers({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log('User fetched successfully')
        });
    }, [fetchUsers, currentPage, filter, sortField, sortDirection]);

    return (
        <UserLayout
            filters={<UserFilterForm onSubmit={handleFilterSubmit}/>}
            addUserButton={
                <Flex justifyContent={"center"}>
                    <AddUserDrawer fetchUsers={fetchUsers}/>
                </Flex>
            }
            table={<UserTable
                users={users}
                onDelete={handleDelete}
                fetchUsers={fetchUsers}
                onSortChange={handleSortChange}
                sortField={sortField}
                sortDirection={sortDirection}
            />}
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
