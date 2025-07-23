import {useDataManagement} from "@/hooks/useDataManagement.ts";
import {deleteContractorById, getContractorsByFilter} from "@/services/contractor-service.ts";
import {FetchableContractorDTO} from "@/types/contractor-types.ts";

export const useContractorManagement = () => {
    return useDataManagement<FetchableContractorDTO>(
        async (params) => {
            const response = await getContractorsByFilter(params);
            return {
                data: response.contractors,
                totalPages: response.totalPages,
            };
        },
        deleteContractorById
    );
};