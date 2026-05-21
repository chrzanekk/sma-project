import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {UnitDTO} from "@/types/unit-types.ts";
import {deleteUnit, getUnitsByFilter} from "@/services/unit-service.ts";
import UnitLayout from "@/components/unit/UnitLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import UnitFilterForm from "@/components/unit/UnitFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import React from "react";
import AddUnitDrawer from "@/components/unit/AddUnitDrawer.tsx";
import GenericUnitTable from "@/components/unit/GenericUnitTable.tsx";

const UnitManagement: React.FC = () => {
    const {
        items: units,
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
    } = useDataManagement<UnitDTO>(
        async (params) => {
            const response = await getUnitsByFilter(params);
            return {
                data: response.units,
                totalPages: response.totalPages,
            };
        },
        deleteUnit,
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
            table={
                <GenericUnitTable
                    units={units}
                    onDelete={onDelete}
                    fetchUnits={() => onPageChange(currentPage)}
                    onSortChange={onSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    extended={true}
                />}
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

export default UnitManagement;

