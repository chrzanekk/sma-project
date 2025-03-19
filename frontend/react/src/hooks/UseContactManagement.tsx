// src/hooks/useContactManagement.ts
import { useCallback, useState } from "react";
import { getContactsByFilter } from "@/services/contact-service";
import { deleteContactById } from "@/services/contact-service"; // lub inny serwis, jeśli masz oddzielny
import { FetchableContactDTO } from "@/types/contact-types";

interface UseContactManagementReturn {
    contacts: FetchableContactDTO[];
    totalPages: number;
    rowsPerPage: number;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    filter: Record<string, any>;
    fetchContacts: (customFilter?: Record<string, any>, page?: number, size?: number) => Promise<void>;
    handleContactDelete: (id: number) => Promise<void>;
    handleContactSortChange: (field: string) => Promise<void>;
    setRowsPerPage: (rows: number) => void;
    setFilter: (filter: Record<string, any>) => void;
}

const useContactManagement = (initialRowsPerPage: number = 10): UseContactManagementReturn => {
    const [contacts, setContacts] = useState<FetchableContactDTO[]>([]);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(initialRowsPerPage);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>({});

    const fetchContacts = useCallback(
        async (customFilter: Record<string, any> = {}, page = 0, size = rowsPerPage) => {
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
                console.error("Error fetching contacts: ", err);
            }
        },
        [rowsPerPage, sortField, sortDirection]
    );

    const handleContactDelete = useCallback(
        async (id: number) => {
            try {
                await deleteContactById(id);
                await fetchContacts({ ...filter, sort: sortField ? `${sortField},${sortDirection}` : undefined }, 0, rowsPerPage);
            } catch (err) {
                console.error("Error deleting contact: ", err);
            }
        },
        [filter, sortField, sortDirection, rowsPerPage, fetchContacts]
    );

    const handleContactSortChange = useCallback(
        async (field: string) => {
            let newDirection: "asc" | "desc" = "asc";
            if (sortField === field) {
                newDirection = sortDirection === "asc" ? "desc" : "asc";
            }
            setSortField(field);
            setSortDirection(newDirection);
            await fetchContacts({ ...filter, sort: `${field},${newDirection}` }, 0, rowsPerPage);
        },
        [sortField, sortDirection, filter, rowsPerPage, fetchContacts]
    );

    return {
        contacts,
        totalPages,
        rowsPerPage,
        sortField,
        sortDirection,
        filter,
        fetchContacts,
        handleContactDelete,
        handleContactSortChange,
        setRowsPerPage,
        setFilter,
    };
};

export default useContactManagement;
