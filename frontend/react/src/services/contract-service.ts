import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {CompanyBaseDTO} from "@/types/company-type.ts";
import {ContractDTO, FetchableContractDTO} from "@/types/contract-types.ts";


const CONTRACT_API_BASE = "/api/contracts";


export const getContractsByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${CONTRACT_API_BASE}?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            contracts: items as FetchableContractDTO[],
            totalPages
        };
    } catch (err) {
        return {contracts: [], totalPages: 1};
    }
}

export const getContractById = async (id: number) => {
    try {
        const response = await api.get(`${CONTRACT_API_BASE}/${id}`, getAuthConfig());
        const contractDTO: FetchableContractDTO = response.data;
        return contractDTO;
    } catch (err) {
        throw err;
    }
}

export const addContract = async (addContract: ContractDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addContract,
            company
        }
        const response = await api.post(`${CONTRACT_API_BASE}`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const updateContract = async (updateContract: ContractDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateContract,
            company
        }
        const response = await api.put(`${CONTRACT_API_BASE}/${updateContract.id}`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteContractById = async (id: number) => {
    await api.delete(`${CONTRACT_API_BASE}/${id}`, getAuthConfig());
}