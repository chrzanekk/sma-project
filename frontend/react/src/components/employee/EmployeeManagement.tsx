import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {EmployeeDTO} from "@/types/employee-types.ts";
import {deleteEmployeeById, getEmployeeByFilter} from "@/services/employee-service.ts";
import EmployeeLayout from "@/components/employee/EmployeeLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import EmployeeFilterForm from "@/components/employee/EmployeeFilterForm.tsx";
import {Box, Flex} from "@chakra-ui/react";
import React from "react";
import AddEmployeeDrawer from "@/components/employee/AddEmployeeDrawer.tsx";
import GenericEmployeeTable from "@/components/employee/GenericEmployeeTable.tsx";

const EmployeeManagement: React.FC = () => {
    const {
        items: employees,
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

    } = useDataManagement<EmployeeDTO>(
        async (params) => {
            const response = await getEmployeeByFilter(params);
            return {
                data: response.employees,
                totalPages: response.totalPages
            }
        },
        deleteEmployeeById,
        {},
        true
    );

    return (
        <Box mt={1}>
            <EmployeeLayout
                filters={<EmployeeFilterForm onSubmit={onFilterSubmit}/>}
                addEmployeeButton={<Flex justify={"center"} gap={2}>
                    <AddEmployeeDrawer fetchEmployees={() => onPageChange(currentPage)}/>
                </Flex>}

                table={<GenericEmployeeTable
                    employees={employees}
                    onDelete={onDelete}
                    onSortChange={onSortChange}
                    fetchEmployees={() => onPageChange(currentPage)}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    extended={true}/>
                }

                pagination={
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        rowsPerPage={rowsPerPage}
                        onPageChange={onPageChange}
                        onRowsPerPageChange={onRowsPerPageChange}/>
                }
            />
        </Box>
    )
};
export default EmployeeManagement;