import React from "react";
import {deleteConstructionSiteById, getConstructionSiteByFilter} from "@/services/construction-site-service.ts";
import ConstructionSiteLayout from "@/components/constructionsite/ConstructionSiteLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import AddRoleDrawer from "@/components/role/AddRoleDrawer.tsx";
import {Flex} from "@chakra-ui/react";
import ConstructionSiteFilterForm from "@/components/constructionsite/ConstructionSiteFilterForm.tsx";
import ConstructionSiteTableWithContractors
    from "@/components/constructionsite/ConstructionSiteTableWithContractors.tsx";
import {useContractorManagement} from "@/hooks/useContractorManagement.tsx";


const ConstructionSiteManagement: React.FC = () => {
    const {
        items: constructionSites,
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
        params => getConstructionSiteByFilter(params).then(res => ({
            data: res.constructionSites, totalPages: res.totalPages
        })),
        deleteConstructionSiteById
    );

    const {
        sortField: contractorSortField,
        sortDirection: contractorSortDirection,
        handlers: {
            onDelete: contractorOnDelete,
            onSortChange: contractorOnSortChange,
            onFilterSubmit: contractorOnFilterSubmit,
        },
    } = useContractorManagement();

    return (
        <ConstructionSiteLayout
            filters={<ConstructionSiteFilterForm onSubmit={onFilterSubmit}/>}
            addConstructionSiteButton={<Flex justifyContent={"center"}>
                <AddRoleDrawer fetchRoles={() => onFilterSubmit({})}/>
            </Flex>
            }
            content={
                <ConstructionSiteTableWithContractors
                    constructionSites={constructionSites}
                    onDelete={onDelete}
                    onSortChange={onSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    contractorFetchContractors={async () => contractorOnFilterSubmit({})}
                    contractorOnDelete={contractorOnDelete}
                    contractorOnSortChange={contractorOnSortChange}
                    contractorSortField={contractorSortField}
                    contractorSortDirection={contractorSortDirection}
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

export default ConstructionSiteManagement;