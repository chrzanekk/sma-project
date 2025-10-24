import {RoleDTO} from "@/types/role-types.ts";
import React from "react";
import {deleteRoleById, getRoleByFilterAndPage} from "@/services/role-service.ts";
import RoleLayout from "@/components/role/RoleLayout.tsx";
import RoleFilterForm from "@/components/role/RoleFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import AddRoleDrawer from "@/components/role/AddRoleDrawer.tsx";
import RoleTable from "@/components/role/RoleTable.tsx";
import {useDataManagement} from "@/hooks/useDataManagement.ts";

const RoleManagement: React.FC = () => {
    const {
        items: roles,
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
    } = useDataManagement<RoleDTO>(
        async (params) => {
            const response = await getRoleByFilterAndPage(params);
            return {
                data: response.roles,
                totalPages: response.totalPages
            };
        },
        deleteRoleById,
        {},
        false
    );

    return (
        <RoleLayout
            filters={<RoleFilterForm onSubmit={onFilterSubmit}/>}
            addRoleButton={<Flex justifyContent={"center"}>
                <AddRoleDrawer fetchRoles={() => onFilterSubmit({})}/>
            </Flex>}
            table={<RoleTable
                roles={roles}
                onDelete={onDelete}
                onSortChange={onSortChange}
                sortField={sortField}
                sortDirection={sortDirection}
            />
            }
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={onPageChange}
                    onRowsPerPageChange={onRowsPerPageChange}
                />}
        />
    );
};

export default RoleManagement;