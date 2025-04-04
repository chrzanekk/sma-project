import React, {useCallback, useEffect, useState} from "react";
import {deleteContractorById} from "@/services/contractor-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import {Flex} from "@chakra-ui/react";
import {FetchableContactDTO} from "@/types/contact-types.ts";
import {getContactsByFilter} from "@/services/contact-service.ts";
import AddContactDialog from "@/components/contact/AddContactDialog.tsx";
import ContactFilterForm from "@/components/contact/ContactFilterForm.tsx";
import ContactLayout from "@/components/contact/ContactLayout.tsx";
import GenericContactTable from "@/components/contact/GenericContactTable.tsx";
import {useCompany} from "@/hooks/useCompany.ts";


const ContactManagement: React.FC = () => {
    const [contacts, setContacts] = useState<FetchableContactDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});
    const {selectedCompany} = useCompany();


    const fetchContacts = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        if(!selectedCompany) return;
        try {
            const response = await getContactsByFilter({
                ...customFilter,
                companyId: selectedCompany.id,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setContacts(response.contacts);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching contacts: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection, selectedCompany])

    const handleRowsPerPageChange = (size: number) => {
        if(!selectedCompany) return;
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchContacts({
            ...filter,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        if(!selectedCompany) return;
        await deleteContractorById(id);
        fetchContacts({
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
        fetchContacts({
            ...values,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        if(!selectedCompany) return;
        setCurrentPage(page);
        fetchContacts({
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
        fetchContacts({
            ...filter,
            companyId: selectedCompany.id,
            sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        if(!selectedCompany) return;
        fetchContacts({
            ...filter,
            companyId: selectedCompany.id,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log("Contacts fetched successfully");
        });
    }, [fetchContacts, currentPage, filter, sortField, sortDirection]);

    return (
        <ContactLayout
            filters={<ContactFilterForm onSubmit={handleFilterSubmit}/>}
            addContactButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddContactDialog fetchContacts={fetchContacts}/>
                </Flex>
            }
            table={
                <GenericContactTable
                    contacts={contacts}
                    onDelete={handleDelete}
                    fetchContacts={fetchContacts}
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

export default ContactManagement;