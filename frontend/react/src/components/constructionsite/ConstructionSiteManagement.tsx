import React from "react";
import {deleteConstructionSiteById, getConstructionSiteByFilter} from "@/services/construction-site-service.ts";
import ConstructionSiteLayout from "@/components/constructionsite/ConstructionSiteLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import {useDataManagement} from "@/hooks/useDataManagement.ts";
import AddRoleDrawer from "@/components/role/AddRoleDrawer.tsx";
import {Flex} from "@chakra-ui/react";
import ConstructionSiteFilterForm from "@/components/constructionsite/ConstructionSiteFilterForm.tsx";
import ConstructionSiteTable from "@/components/constructionsite/ConstructionSiteTable.tsx";


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

    return (
        <ConstructionSiteLayout
            filters={<ConstructionSiteFilterForm onSubmit={onFilterSubmit}/>}
            addConstructionSiteButton={<Flex justifyContent={"center"}>
                <AddRoleDrawer fetchRoles={() => onFilterSubmit({})}/>
            </Flex>}
            content={
                <ConstructionSiteTable
                    constructionSites={constructionSites}
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
                />
            }
        />
    );
};

export default ConstructionSiteManagement;