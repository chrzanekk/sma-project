import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {ContractorDTO, ContractorUpdateDTO, FetchableContractorDTO} from "@/types/contractor-types.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils";
import {CompanyBaseDTO} from "@/types/company-type.ts";
import {ContactDTO} from "@/types/contact-types.ts";

const CONTRACTOR_API_BASE = "/api/contractors";

export const getContractorsByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0,
            sort: filter.sort || 'id,asc'
        });
        const response = await api.get(`${CONTRACTOR_API_BASE}/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        console.table(response.data);
        return  {
            contractors: items as FetchableContractorDTO[],
            totalPages
        };
    } catch (err) {
        return {contractors: [], totalPages: 1};
    }
}

export const getContactsByContractorIdPaged = async (
    contractorId: number,
    page: number,
    size: number
) => {
    const response = await api.get(`${CONTRACTOR_API_BASE}/contractor/${contractorId}/contacts`, {
        params: {
            contractorId,
            page,
            size,
            sort: 'id,asc',
        },
        ...getAuthConfig(),
    });

    return parsePaginationResponse<ContactDTO>(response);
};


export const getContractorById = async (id: number) => {
    try {
        const response = await api.get(`${CONTRACTOR_API_BASE}/getById/${id}`, getAuthConfig());
        const contractorDTO: FetchableContractorDTO = response.data;
        return contractorDTO;
    } catch (err) {
        throw err;
    }
}

export const addContractor = async (addContractor: ContractorDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addContractor,
            company
        }
        const response = await api.post(`${CONTRACTOR_API_BASE}/add`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const updateContractor = async (updateContractor: ContractorUpdateDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateContractor,
            company
        }
        const response = await api.put(`${CONTRACTOR_API_BASE}/update`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteContractorById = async (id: number) => {
    await api.delete(`${CONTRACTOR_API_BASE}/delete/${id}`, getAuthConfig());
}