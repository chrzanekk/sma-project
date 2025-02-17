import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {ContractorDTO, AddContractorDTO, EditContractorDTO} from "@/types/contractor-types.ts";


const CONTRACTOR_API_BASE = "/api/contractors";

export const getContractorsByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${CONTRACTOR_API_BASE}/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        const result = {
            contractors: items as ContractorDTO[],
            totalPages
        };
        return result;
    } catch (err) {
        return {contractors: [], totalPages: 1};
    }
}

export const getContractorById = async (id: number) => {
    try {
        const response = await api.get(`${CONTRACTOR_API_BASE}/getById/${id}`, getAuthConfig());
        const contractorDTO: ContractorDTO = response.data;
        console.log(contractorDTO)
        return contractorDTO;
    } catch (err) {
        throw err;
    }
}

export const addContractor = async (addContractor: AddContractorDTO) => {
    try {
        const response = await api.post(`${CONTRACTOR_API_BASE}/add`, addContractor, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const updateContractor = async (updateContractor: EditContractorDTO) => {
    try {
        const response = await api.put(`${CONTRACTOR_API_BASE}/update`, updateContractor, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteContractorById = async (id: number) => {
    await api.delete(`${CONTRACTOR_API_BASE}/delete/${id}`, getAuthConfig());
}