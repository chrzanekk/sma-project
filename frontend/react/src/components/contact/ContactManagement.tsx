import React, {useCallback, useEffect, useState} from "react";
import {deleteContractorById} from "@/services/contractor-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import {Flex} from "@chakra-ui/react";
import {FetchableContactDTO} from "@/types/contact-types.ts";
import {getContactsByFilter} from "@/services/contact-service.ts";
import AddContactDrawer from "@/components/contact/AddContactDrawer.tsx";
import AddContactDialog from "@/components/contact/AddContactDialog.tsx";
import ContactTable from "@/components/contact/ContactTable.tsx";
import ContactFilterForm from "@/components/contact/ContactFilterForm.tsx";
import ContactLayout from "@/components/contact/ContactLayout.tsx";


const ContactManagement: React.FC = () => {
    const [contacts, setContacts] = useState<FetchableContactDTO[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});

    const fetchContacts = useCallback(async (customFilter = {}, page = 0, size = rowsPerPage) => {
        try {
            const response = await getContactsByFilter({
                ...customFilter,
                page,
                size,
                sort: sortField ? `${sortField},${sortDirection}` : undefined,
            });
            setContacts(response.contacts);
            setTotalPages(response.totalPages);
        } catch (err) {
            console.error('Error fetching contractors: ', err);
        }
    }, [rowsPerPage, sortField, sortDirection])

    const handleRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        fetchContacts({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, size).catch(() => {
        });
    };

    const handleDelete = async (id: number) => {
        await deleteContractorById(id);
        fetchContacts({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage, rowsPerPage).catch(() => {
        });
    };

    const handleFilterSubmit = (values: Record<string, any>) => {
        setCurrentPage(0);
        setFilter(values);
        fetchContacts({
            ...values,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, 0, rowsPerPage).catch(() => {
        });
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        fetchContacts({
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
        fetchContacts({...filter, sort: `${field},${newDirection}`}, 0, rowsPerPage).catch(() => {
        });
    }

    useEffect(() => {
        fetchContacts({
            ...filter,
            sort: sortField ? `${sortField},${sortDirection}` : undefined
        }, currentPage).then(() => {
            console.log("Contractors fetched successfully");
        });
    }, [fetchContacts, currentPage, filter, sortField, sortDirection]);

    return (
        <ContactLayout
            filters={<ContactFilterForm onSubmit={handleFilterSubmit}/>}
            addContactButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddContactDrawer fetchContacts={fetchContacts}/>
                    <AddContactDialog fetchContacts={fetchContacts}/>
                </Flex>
            }
            table={
                <ContactTable
                    contacts={contacts}
                    onDelete={handleDelete}
                    fetchContacts={fetchContacts}
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

export default ContactManagement;