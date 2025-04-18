import {FetchableContractorDTO} from "@/types/contractor-types.ts";
import React, {useCallback, useEffect, useState} from "react";
import {deleteContractorById, getContractorsByFilter} from "@/services/contractor-service.ts";
import ContractorLayout from "@/components/contractor/ContractorLayout.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import ContractorFilterForm from "@/components/contractor/ContractorFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import AddContractorWithContactDialog from "@/components/contractor/AddContractorWithContactDialog.tsx";
import ContractorTableWithContacts from "@/components/contractor/ContractorTableWithContacts.tsx";
import useContactManagement from "@/hooks/UseContactManagement.tsx";
import {useCompany} from "@/hooks/useCompany";

const ContractorManagement: React.FC = () => {
    const [contractors, setContractors] = useState<FetchableContractorDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});
    const {selectedCompany} = useCompany();


    const {
        fetchContacts,
        handleContactDelete,
        handleContactSortChange,
    } = useContactManagement();

    const fetchContractors = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        if(!selectedCompany) return;

        try {
            const response = await getContractorsByFilter({
                ...customFilter,
                companyId: selectedCompany.id,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setContractors(response.contractors);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching contractors: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection, selectedCompany])

    const handleRowsPerPageChange = (size: number) => {
        if(!selectedCompany) return;
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchContractors({
            ...filter,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        if(!selectedCompany) return;
        await deleteContractorById(id);
        fetchContractors({
            ...filter,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage, rowsPerPage).catch(() => {
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        if(!selectedCompany) return;
        setCurrentPage(0);
        setFilter(values);
        fetchContractors({
            ...values,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        if(!selectedCompany) return;
        setCurrentPage(page);
        fetchContractors({
            ...filter,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, page, rowsPerPage).catch(() => {
        });
    };

    const handleSortChange = (field: string) => {
        if(!selectedCompany) return;
        let newDirection: "asc" | "desc" = "asc";
        if (sortField === field) {
            newDirection = sortDirection === "asc" ? "desc" : "asc";
        }
        setSortField(field);
        setSortDirection(newDirection);
        fetchContractors({
            ...filter,
            companyId: selectedCompany.id,
            sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        if(!selectedCompany) return;
        fetchContractors({
            ...filter,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log("Contractors fetched successfully");
        });
    }, [fetchContractors, currentPage, filter, sortField, sortDirection, selectedCompany]);

    return (
        <ContractorLayout
            filters={<ContractorFilterForm onSubmit={handleFilterSubmit}/>}
            addContractorButton={
                <Flex justifyContent={"center"}>
                    <AddContractorWithContactDialog fetchContractors={fetchContractors}/>
                </Flex>
            }
            table={
                <ContractorTableWithContacts
                    contractors={contractors}
                    onDelete={handleDelete}
                    fetchContractors={fetchContractors}
                    onSortChange={handleSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    contactFetchContacts={fetchContacts}
                    contactOnDelete={handleContactDelete}
                    contactOnSortChange={handleContactSortChange}
                    contactSortField={sortField}
                    contactSortDirection={sortDirection}
                />
            }
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

export default ContractorManagement;