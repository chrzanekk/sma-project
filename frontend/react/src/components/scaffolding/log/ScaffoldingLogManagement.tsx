import React from "react";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {Box, Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import {deleteScaffoldingLog, getScaffoldingLogByFilter} from "@/services/scaffolding-log-service.ts";
import ScaffoldingLogFilterForm from "@/components/scaffolding/log/ScaffoldingLogFilterForm.tsx";
import ScaffoldingLogLayout from "@/components/scaffolding/log/ScaffoldingLogLayout.tsx";
import AddScaffoldingLogDialog from "@/components/scaffolding/log/AddScaffoldingLogDialog.tsx";
import ScaffoldingLogView from "@/components/scaffolding/log/ScaffoldingLogView.tsx";


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
    } = useDataManagement(
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
        <Box mt={1}>
            <ScaffoldingLogLayout
                filters={<ScaffoldingLogFilterForm
                    onSubmit={onFilterSubmit}
                />}
                addLogButton={
                    <Flex justify={"center"} gap={2}>
                        <AddScaffoldingLogDialog fetchLogs={() => onPageChange(currentPage)}/>
                    </Flex>
                }
                table={
                    <ScaffoldingLogView
                        logs={logs}
                        onDelete={onDelete}
                        fetchLogs={() => onPageChange(0)}
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
        </Box>
    )
};

export default ScaffoldingLogManagement;