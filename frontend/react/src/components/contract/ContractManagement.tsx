import React from "react";
import Pagination from "@/components/shared/Pagination.tsx";
import {Flex} from "@chakra-ui/react";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {deleteContractById, getContractsByFilter} from "@/services/contract-service.ts";
import GenericContractTable from "@/components/contract/GenericContractTable.tsx";
import ContractFilterForm from "@/components/contract/ContractFilterForm.tsx";
import ContractLayout from "@/components/contract/ContractLayout.tsx";
import AddContractDialog from "@/components/contract/AddContractDialog.tsx";


const ContractManagement: React.FC = () => {
    const {
        items: contracts,
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
        params =>
            getContractsByFilter(params).then(res => ({
                data: res.contracts,
                totalPages: res.totalPages,
            })),
        deleteContractById,
    );

    return (
        <ContractLayout
            filters={<ContractFilterForm onSubmit={onFilterSubmit}/>}
            addContractButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddContractDialog fetchContracts={() => onPageChange(0)}/>
                </Flex>
            }
            table={
                <GenericContractTable
                    contracts={contracts}
                    onDelete={onDelete}
                    fetchContracts={() => onPageChange(0)}
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
                />
            }
        />
    );
};

export default ContractManagement;