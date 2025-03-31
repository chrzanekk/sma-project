import React, {useCallback, useEffect, useState} from "react";
import {deleteContractorById} from "@/services/contractor-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import {Flex} from "@chakra-ui/react";
import {FetchableCompanyDTO} from "@/types/company-type.ts";
import {getCompaniesByFilter} from "@/services/company-service.ts";
import CompanyFilterForm from "@/components/company/CompanyFilterForm.tsx";
import AddCompanyDialog from "@/components/company/AddCompanyDialog.tsx";
import GenericCompanyTable from "@/components/company/GenericCompanyTable.tsx";
import CompanyLayout from "@/components/company/CompanyLayout.tsx";

const CompanyManagement: React.FC = () => {
    const [companies, setCompanies] = useState<FetchableCompanyDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});

    const fetchCompanies = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getCompaniesByFilter({
                ...customFilter,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setCompanies(response.companies);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching contacts: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection])

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchCompanies({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        await deleteContractorById(id);
        fetchCompanies({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage, rowsPerPage).catch(() => {
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        setFilter(values);
        fetchCompanies({
            ...values,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchCompanies({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, page, rowsPerPage).catch(() => {
        });
    };

    const handleSortChange = (field: string) => {
        let newDirection: "asc" | "desc" = "asc";
        if (sortField === field) {
            newDirection = sortDirection === "asc" ? "desc" : "asc";
        }
        setSortField(field);
        setSortDirection(newDirection);
        fetchCompanies({...filter, sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        fetchCompanies({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log("Companies fetched successfully");
        });
    }, [fetchCompanies, currentPage, filter, sortField, sortDirection]);

    return (
        <CompanyLayout
            filters={<CompanyFilterForm onSubmit={handleFilterSubmit}/>}
            addCompanyButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddCompanyDialog fetchCompanies={fetchCompanies}/>
                </Flex>
            }
            table={
                <GenericCompanyTable
                    companies={companies}
                    onDelete={handleDelete}
                    fetchCompanies={fetchCompanies}
                    onSortChange={handleSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    extended={true}
                />}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={handlePageChange}
                    onRowsPerPageChange={handleRowsPerPageChange}
                />
            }
        />
    );
};

export default CompanyManagement;