import React from "react";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import UnitLayout from "@/components/unit/UnitLayout.tsx";
import {Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import {ScaffoldingLogDTO} from "@/types/scaffolding-log-types.ts";
import {deleteScaffoldingLog, getScaffoldingLogByFilter} from "@/services/scaffolding-log-service.ts";
import ScaffoldingLogFilterForm from "@/components/scaffolding/log/ScaffoldingLogFilterForm.tsx";


const ScaffoldingLogManagement: React.FC = () => {
    const {
        items: logs,
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
    } = useDataManagement<ScaffoldingLogDTO>(
        async (params) => {
            const response = await getScaffoldingLogByFilter(params);
            return {
                data: response.logs,
                totalPages: response.totalPages,
            };
        },
        deleteScaffoldingLog,
        {},
        true
    );

    return (
        <UnitLayout
            filters={<ScaffoldingLogFilterForm
                onSubmit={onFilterSubmit}
            />}
            addUnitButton={
                <Flex justify={"center"} gap={2}>
                    {/*<AddUnitDrawer fetchUnits={() => onPageChange(currentPage)}/>*/}
                </Flex>
            }
            {/*TODO not table, develop new type of data show*/}
            table={}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={onPageChange}
                    onRowsPerPageChange={onRowsPerPageChange}
                />}
        />
    )
};
export default ScaffoldingLogManagement;