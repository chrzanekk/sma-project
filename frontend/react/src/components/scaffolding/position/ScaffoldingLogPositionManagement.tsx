import React from "react";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import ScaffoldingLogPositionFilterForm from "@/components/scaffolding/position/ScaffoldingLogPositionFilterForm.tsx";
import {
    deleteScaffoldingLogPosition,
    getScaffoldingLogPositionByFilter
} from "@/services/scaffolding-log-position-service.ts";
import {ScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";
import ScaffoldingLogPositionLayout from "@/components/scaffolding/position/ScaffoldingLogPositionLayout.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import AddScaffoldingLogPositionDialog from "@/components/scaffolding/position/AddScaffoldingLogPositionDialog.tsx";


const ScaffoldingLogPositionManagement: React.FC = () => {
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
    } = useDataManagement<ScaffoldingLogPositionDTO>(
        async (params) => {
            const response = await getScaffoldingLogPositionByFilter(params);
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
            /*'TODO table'*/
            // table={}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={onPageChange}
                    onRowsPerPageChange={onRowsPerPageChange}
                />}
            bgColor={useThemeColors().bgColorPrimary}
        />
    )
};

export default ScaffoldingLogPositionManagement;