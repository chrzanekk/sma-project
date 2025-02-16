import {ContractorDTO} from "@/types/contractor-types.ts";
import React, {useCallback, useEffect, useState} from "react";
import {getContractorsByFilter} from "@/services/contractor-service.ts";
import {deleteUserById} from "@/services/user-service.ts";
import ContractorLayout from "@/components/contractor/ContractorLayout.tsx";
import ContractorTable from "@/components/contractor/ContractorTable.tsx";
import Pagination from "@/components/shared/Pagination.tsx";
import ContractorFilterForm from "@/components/contractor/ContractorFilterForm.tsx";
import {Flex} from "@chakra-ui/react";
import AddContractorDrawer from "@/components/contractor/AddContractorDrawer.tsx";


const ContractorManagement: React.FC = () => {
    const [contractors, setContractors] = useState<ContractorDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const fetchContractors = useCallback(async (filter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getContractorsByFilter({...filter, page, size});
            setContractors(response.contractors);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching contractors: ', err);
        }
    }, [rowsPerPage])

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchContractors({}, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        await deleteUserById(id);
        fetchContractors().catch(() => {
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        fetchContractors(values, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchContractors({}, page, rowsPerPage).catch(() => {
        });
    };

    useEffect(() => {
        fetchContractors({}, currentPage).then(() => {
            console.log('User fetched successfully')
        });
    }, [fetchContractors, currentPage]);

    return (
        <ContractorLayout
            filters={<ContractorFilterForm onSubmit={handleFilterSubmit}/>}
            addContractorButton={
                <Flex justifyContent={"center"}>
                    <AddContractorDrawer fetchContractors={fetchContractors}/>
                </Flex>
            }
            table={
                <ContractorTable
                    contractors={contractors}
                    onDelete={handleDelete}
                    fetchContractors={fetchContractors}
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