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
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});

    const fetchRoles = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getRoleByFilterAndPage({
                ...customFilter,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setRoles(response.roles);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching roles: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection]);

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchRoles({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    }

    const handleDelete = async (id: number) => {
        await deleteRoleById(id);
        fetchRoles({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }).catch(() => {
        });
    }

    const handlePageChange = (page: number) => {
        if (page >= 0 && page < totalPages) {
            setCurrentPage(page);
            fetchRoles({
                ...filter,
                sort: sortField ? `${sortField},${sortDirection}` : undefined
            }, page, rowsPerPage).catch(() => {
            });
        }
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        setFilter(values);
        fetchRoles({
            ...values,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    }

    const handleSortChange = (field: string) => {
        let newDirection: "asc" | "desc" = "asc";
        if (sortField === field) {
            newDirection = sortDirection === "asc" ? "desc" : "asc";
        }
        setSortField(field);
        setSortDirection(newDirection);
        fetchRoles({...filter, sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        fetchRoles({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log('Roles fetched successfully')
        });
    }, [fetchRoles, currentPage, filter, sortField, sortDirection]);

    return (
        <RoleLayout
            filters={<RoleFilterForm onSubmit={handleFilterSubmit}/>}
            addRoleButton={<Flex justifyContent={"center"}>
                <AddRoleDrawer fetchRoles={fetchRoles}/>
            </Flex>}
            table={<RoleTable
                roles={roles}
                onDelete={handleDelete}
                onSortChange={handleSortChange}
                sortField={sortField}
                sortDirection={sortDirection}
            />
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