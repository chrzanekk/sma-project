import {RoleDTO} from "@/types/role-types.ts";
import React, {useCallback, useEffect, useState} from "react";
import {deleteRoleById, getRoleByFilterAndPage} from "@/services/role-service.ts";
import RoleLayout from "@/components/role/RoleLayout.tsx";
import RoleFilterForm from "@/components/role/RoleFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import AddRoleDrawer from "@/components/role/AddRoleDrawer.tsx";
import RoleTable from "@/components/role/RoleTable.tsx";

const RoleManagement: React.FC = () => {
    const [roles, setRoles] = useState<RoleDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const fetchRoles = useCallback(async (filter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getRoleByFilterAndPage({...filter, page, size});
            setRoles(response.roles);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching roles: ', err);
        }
    }, [rowsPerPage]);

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchRoles({}, 0, size).catch(() => {
        });
    }

    const handleDelete = async (id: number) => {
        await deleteRoleById(id);
        fetchRoles().catch(() => {
        });
    }

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchRoles({}, page, rowsPerPage).catch(() => {
        });
    }

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        fetchRoles(values, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        fetchRoles({}, currentPage).then(() => {
            console.log('Roles fetched successfully')
        });
    }, [fetchRoles, currentPage]);

    return (
        <RoleLayout
            filters={<RoleFilterForm onSubmit={handleFilterSubmit}/>}
            addRoleButton={<Flex justifyContent={"center"}>
                <AddRoleDrawer fetchRoles={fetchRoles}/>
            </Flex>}
            table={<RoleTable
                roles={roles}
                onDelete={handleDelete}
                fetchRoles={fetchRoles}/>
            }
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handlePageChange}
                    onRowsPerPageChange={handleRowsPerPageChange}
                />}
        />
    );
};

export default RoleManagement;