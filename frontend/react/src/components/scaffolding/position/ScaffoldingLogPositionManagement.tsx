import React from "react";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {Box, Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import ScaffoldingLogPositionFilterForm from "@/components/scaffolding/position/ScaffoldingLogPositionFilterForm.tsx";
import {
    deleteScaffoldingLogPosition,
    getScaffoldingLogPositionByFilter
} from "@/services/scaffolding-log-position-service.ts";
import {FetchableScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";
import ScaffoldingLogPositionLayout from "@/components/scaffolding/position/ScaffoldingLogPositionLayout.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import AddScaffoldingLogPositionDialog from "@/components/scaffolding/position/AddScaffoldingLogPositionDialog.tsx";
import ScaffoldingLogPositionTable from "@/components/scaffolding/position/ScaffoldingLogPositionTable.tsx";
import {useLocation, useParams} from "react-router-dom";


const ScaffoldingLogPositionManagement: React.FC = () => {

    const {logId} = useParams<{ logId: string }>();
    const location = useLocation();

    const logName = location.state?.logName ?? "";

    const {
        items: logPositions,
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
    } = useDataManagement<FetchableScaffoldingLogPositionDTO>(
        async (params) => {
            const fetchParams = {
                ...params,
                scaffoldingLogId: logId ? Number(logId) : undefined
            };
            const response = await getScaffoldingLogPositionByFilter(fetchParams);
            return {
                data: response.logs,
                totalPages: response.totalPages,
            };
        },
        deleteScaffoldingLogPosition,
        {},
        true
    );

    return (
        <Box mt={1}>
            <ScaffoldingLogPositionLayout
                filters={<ScaffoldingLogPositionFilterForm
                    onSubmit={onFilterSubmit}
                    isFullLogPositionList={false}
                />}
                addLogPositionButton={
                    <Flex justify={"center"} gap={2}>
                        <AddScaffoldingLogPositionDialog fetchPositions={() => onPageChange(currentPage)}/>
                    </Flex>
                }
                table={<ScaffoldingLogPositionTable
                    positions={logPositions}
                    onDelete={onDelete}
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
                    />}
                bgColor={useThemeColors().bgColorPrimary}
                scaffoldingLogName={logName}
            />

        </Box>
    )
};

export default ScaffoldingLogPositionManagement;