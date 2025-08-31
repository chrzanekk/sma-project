import React from "react";
import Pagination from "@/components/shared/Pagination.tsx";
import {Flex} from "@chakra-ui/react";
import {FetchableCompanyDTO} from "@/types/company-type.ts";
import {deleteCompanyById, getCompaniesByFilter} from "@/services/company-service.ts";
import CompanyFilterForm from "@/components/company/CompanyFilterForm.tsx";
import AddCompanyDialog from "@/components/company/AddCompanyDialog.tsx";
import GenericCompanyTable from "@/components/company/GenericCompanyTable.tsx";
import CompanyLayout from "@/components/company/CompanyLayout.tsx";
import {useDataManagement} from "@/hooks/useDataManagement";

const CompanyManagement: React.FC = () => {
    const {
        items: companies,
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
    } = useDataManagement<FetchableCompanyDTO>(
        async (params) => {
            const response = await getCompaniesByFilter(params);
            return {
                data: response.companies,
                totalPages: response.totalPages
            };
        },
        deleteCompanyById,
        {}
        ,false
    );

    return (
        <CompanyLayout
            filters={
                <CompanyFilterForm onSubmit={onFilterSubmit}/>
            }
            addCompanyButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddCompanyDialog
                        fetchCompanies={() => onFilterSubmit({})}
                    />
                </Flex>
            }
            table={
                <GenericCompanyTable
                    companies={companies}
                    onDelete={onDelete}
                    fetchCompanies={() => onFilterSubmit({})}
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
                />
            }
        />
    );
};

export default CompanyManagement;