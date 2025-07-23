import {FetchableContractorDTO} from "@/types/contractor-types.ts";
import React from "react";
import {deleteContractorById, getContractorsByFilter} from "@/services/contractor-service.ts";
import ContractorLayout from "@/components/contractor/ContractorLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import ContractorFilterForm from "@/components/contractor/ContractorFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import AddContractorWithContactDialog from "@/components/contractor/AddContractorWithContactDialog.tsx";
import ContractorTableWithContacts from "@/components/contractor/ContractorTableWithContacts.tsx";
import {useContactManagement} from "@/hooks/UseContactManagement.tsx";
import {useDataManagement} from "@/hooks/useDataManagement.ts";

const ContractorManagement: React.FC = () => {
    const {
        items: contractors,
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
    } = useDataManagement<FetchableContractorDTO>(
        // funkcja pobierająca dane
        async (params) => {
            const response = await getContractorsByFilter(params);
            return {
                data: response.contractors,
                totalPages: response.totalPages
            };
        },
        // funkcja usuwająca
        deleteContractorById
    );

    const {
        sortField: contactSortField,
        sortDirection: contactSortDirection,
        handlers: {
            onDelete: contactOnDelete,
            onSortChange: contactOnSortChange,
            onFilterSubmit: contactOnFilterSubmit,
        },
    } = useContactManagement();


    return (
        <ContractorLayout
            filters={<ContractorFilterForm onSubmit={onFilterSubmit}/>}
            addContractorButton={
                <Flex justifyContent={"center"}>
                    <AddContractorWithContactDialog fetchContractors={() => onFilterSubmit({})}/>
                </Flex>
            }
            table={
                <ContractorTableWithContacts
                    contractors={contractors}
                    onDelete={onDelete}
                    fetchContractors={() => onFilterSubmit({})}
                    onSortChange={onSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    contactFetchContacts={async () => contactOnFilterSubmit({})}
                    contactOnDelete={contactOnDelete}
                    contactOnSortChange={contactOnSortChange}
                    contactSortField={contactSortField}
                    contactSortDirection={contactSortDirection}
                />
            }
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

export default ContractorManagement;