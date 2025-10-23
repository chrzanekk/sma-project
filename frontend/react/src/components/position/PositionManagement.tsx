import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {PositionDTO} from "@/types/position-types.ts";
import {deletePositonById, getPositionByFilter} from "@/services/position-service.ts";
import PositionLayout from "@/components/position/PositionLayout.tsx";
import React from "react";
import PositionFilterForm from "@/components/position/PositionFilterForm.tsx";
import AddPositionDrawer from "@/components/position/AddPositionDrawer.tsx";
import {Flex} from "@chakra-ui/react";
import Pagination from "@/components/shared/Pagination.tsx";
import GenericPositionTable from "@/components/position/GenericPositionTable.tsx";

const PositionManagement: React.FC = () => {
    const {
        items: positions,
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
    } = useDataManagement<PositionDTO>(
        async (params) => {
            const response = await getPositionByFilter(params);
            return {
                data: response.positions,
                totalPages: response.totalPages,
            };
        },
        deletePositonById,
        {},
        true
    );

    return (
        <PositionLayout
            filters={<PositionFilterForm onSubmit={onFilterSubmit}/>}
            addPositionButton={
                <Flex justify={"center"} gap={2}>
                    <AddPositionDrawer fetchPositions={() => onPageChange(currentPage)}/>
                </Flex>
            }
            table={
                <GenericPositionTable
                    positions={positions}
                    onDelete={onDelete}
                    fetchPositions={() => onPageChange(currentPage)}
                    onSortChange={onSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    extended={true}
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
    )
};

export default PositionManagement;