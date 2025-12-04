import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {UnitDTO} from "@/types/unit-types.ts";
import {getUnitsByFilter} from "@/services/unit-service.ts";
import {deletePositonById} from "@/services/position-service.ts";
import UnitLayout from "@/components/unit/UnitLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import UnitFilterForm from "@/components/unit/UnitFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import React from "react";
import AddUnitDrawer from "@/components/unit/AddUnitDrawer.tsx";

const UnitManagement: React.FC = () => {
    const {
        items: units,
        currentPage,
        totalPages,
        rowPerPage,
        sortField,
        sortDirection,
        handlers: {
            onPageChange,
            onRowsPerPageChange,
            onSortChange,
            onFilterSubmit,
            onDelete
        }
    } = useDataManagement<UnitDTO>(
        async (params) => {
            const response = await getUnitsByFilter(params);
            return {
                data: response.units,
                totalPages: response.totalPages,
            };
        },
        deletePositonById,
        {},
        true
    );

    return (
        <UnitLayout
            filters={<UnitFilterForm onSubmit={onFilterSubmit}/>}
            addUnitButton={
                <Flex justify={"center"} gap={2}>
                    <AddUnitDrawer fetchUnits={() => onPageChange(currentPage)}/>
                </Flex>}
            // table={}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowPerPage}
                    onPageChange={onPageChange}
                    onRowsPerPageChange={onRowsPerPageChange}
                />}
        />
    )
};

export default UnitManagement;

