// hooks/useDataManagement.ts
import {useCallback, useEffect, useRef, useState} from "react";
import {useCompany} from "@/hooks/useCompany.ts";

export type FetchParams = {
    page?: number;
    size?: number;
    sort?: string;
    [key: string]: any;
};

export interface DataManagementResult<T> {
    items: T[];
    currentPage: number;
    totalPages: number;
    totalCount: number;
    rowsPerPage: number;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    filter: Record<string, any>;
    handlers: {
        onPageChange: (page: number) => void;
        onRowsPerPageChange: (size: number) => void;
        onSortChange: (field: string) => void;
        onFilterSubmit: (values: Record<string, any>) => void;
        onDelete: (id: number) => Promise<void>;
    };
}

/**
 * @param fetchFn  async (params: FetchParams) => { data: T[]; totalPages: number }
 * @param deleteFn async (id: number) => void
 * @param initialFilter
 * @param useCompanyId
 */
export function useDataManagement<T>(
    fetchFn: (params: FetchParams) => Promise<{ data: T[]; totalPages: number, totalCount: number }>,
    deleteFn: (id: number) => Promise<void>,
    initialFilter: Record<string, any> = {},
    useCompanyId: boolean = true,
): DataManagementResult<T> {
    const {selectedCompany} = useCompany();

    const [items, setItems] = useState<T[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<string | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const [filter, setFilter] = useState<Record<string, any>>(initialFilter);
    const [totalCount, setTotalCount] = useState(0);

    const isFirstRender = useRef(true);

    const doFetch = useCallback(async (page = currentPage, size = rowsPerPage, filt = filter) => {
        if (useCompanyId && !selectedCompany) return;
        const sortParam = sortField ? `${sortField},${sortDirection}` : undefined;
        const params: FetchParams = {
            ...filt,
            page,
            size,
            sort: sortParam,
            ...(useCompanyId && selectedCompany ? {companyId: selectedCompany.id} : {}),
        };
        const {data, totalPages, totalCount} = await fetchFn(params);
        setItems(data);
        setTotalPages(totalPages);
        setTotalCount(totalCount)
    }, [fetchFn, selectedCompany, currentPage, rowsPerPage, sortField, sortDirection, filter, useCompanyId]);

    const onPageChange = (page: number) => {
        setCurrentPage(page);
        doFetch(page).then();
    };

    const onRowsPerPageChange = (size: number) => {
        setRowsPerPage(size);
        setCurrentPage(0);
        doFetch(0, size).then();
    };

    const onSortChange = (field: string) => {
        const newDir = sortField === field && sortDirection === "asc" ? "desc" : "asc";
        setSortField(field);
        setSortDirection(newDir);
        setCurrentPage(0);
        doFetch(0, rowsPerPage, filter).then();
    };

    const onFilterSubmit = (values: Record<string, any>) => {
        setFilter(values);
        setCurrentPage(0);
        doFetch(0, rowsPerPage, values).then();
    };

    const onDelete = async (id: number) => {
        await deleteFn(id);
        doFetch(currentPage, rowsPerPage).then();
    };
    useEffect(() => {
        // Pomiń pierwsze renderowanie jeśli nie używamy companyId
        if (!useCompanyId) {
            if (isFirstRender.current) {
                isFirstRender.current = false;
                doFetch(0, rowsPerPage, filter).then();
            }
            return;
        }

        // Dla useCompanyId - fetchuj gdy zmienia się company
        if (selectedCompany) {
            setCurrentPage(0);
            doFetch(0, rowsPerPage, filter).then();
        }
    }, [selectedCompany?.id]);

    useEffect(() => {
        if (!useCompanyId && isFirstRender.current) {
            isFirstRender.current = false;
            doFetch(0, rowsPerPage, filter).then();
        }
    }, []);


    return {
        items,
        currentPage,
        totalPages,
        totalCount,
        rowsPerPage,
        sortField,
        sortDirection,
        filter,
        handlers: {
            onPageChange,
            onRowsPerPageChange,
            onSortChange,
            onFilterSubmit,
            onDelete,
        },
    };
}
