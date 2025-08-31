// src/hooks/useContactManagement.ts
import {deleteContactById, getContactsByFilter} from "@/services/contact-service"; // lub inny serwis, jeśli masz oddzielny
import {FetchableContactDTO} from "@/types/contact-types";
import {useDataManagement} from "@/hooks/useDataManagement";

export const useContactManagement = () => {
    return useDataManagement<FetchableContactDTO>(
        async (params) => {
            const response = await getContactsByFilter(params);
            return {
                data: response.contacts,
                totalPages: response.totalPages,
            };
        },
        deleteContactById
    );
};