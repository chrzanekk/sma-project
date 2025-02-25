import {FetchableContractorDTO} from "@/types/contractor-types.ts";
import React, {useCallback, useEffect, useState} from "react";
import {deleteContractorById, getContractorsByFilter} from "@/services/contractor-service.ts";
import ContractorLayout from "@/components/contractor/ContractorLayout.tsx";
import ContractorTable from "@/components/contractor/ContractorTable.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import ContractorFilterForm from "@/components/contractor/ContractorFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import AddContractorDrawer from "@/components/contractor/AddContractorDrawer.tsx";
import AddContractorDialog from "@/components/contractor/AddContractorDialog.tsx";
import AddContractorWithContactDialog from "@/components/contractor/AddContractorWithContactDialog.tsx";


const ContractorManagement: React.FC = () => {
    const [contractors, setContractors] = useState<FetchableContractorDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});

    const fetchContractors = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getContractorsByFilter({
                ...customFilter,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setContractors(response.contractors);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching contractors: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection])

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchContractors({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        await deleteContractorById(id);
        fetchContractors({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage, rowsPerPage).catch(() => {
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        setFilter(values);
        fetchContractors({
            ...values,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchContractors({
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
        fetchContractors({...filter, sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        fetchContractors({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log("Contractors fetched successfully");
        });
    }, [fetchContractors, currentPage, filter, sortField, sortDirection]);

    return (
        <ContractorLayout
            filters={<ContractorFilterForm onSubmit={handleFilterSubmit}/>}
            addContractorButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddContractorDrawer fetchContractors={fetchContractors}/>
                    <AddContractorDialog fetchContractors={fetchContractors}/>
                    <AddContractorWithContactDialog fetchContractors={fetchContractors}/>
                </Flex>
            }
            table={
                <ContractorTable
                    contractors={contractors}
                    onDelete={handleDelete}
                    fetchContractors={fetchContractors}
                    onSortChange={handleSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
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

export default ContractorManagement;