import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {CompanyBaseDTO, FetchableCompanyDTO} from "@/types/company-type.ts";

const COMPANY_API_BASE = "/api/companies";

export const getCompaniesByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            size: filter.size || 10,
            page: filter.page || 0,
            sort: filter.sort || 'id,asc'
        });
        const response = await api.get(`${COMPANY_API_BASE}?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            companies: items as FetchableCompanyDTO[],
            totalPages
        };
    } catch (err) {
        return {companies: [], totalPages: 1};
    }
}

export const getCompanyById = async (id: number) => {
    try {
        const response = await api.get(`${COMPANY_API_BASE}/${id}`, getAuthConfig());
        const companyDTO: FetchableCompanyDTO = response.data;
        return companyDTO;
    } catch (err) {
        throw err;
    }
}

export const addCompany = async (addCompany: CompanyBaseDTO) => {
    try {
        const response = await api.post(`${COMPANY_API_BASE}`, addCompany, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}
export const updateCompany = async (updateCompany: CompanyBaseDTO) => {
    try {
        const response = await api.put(`${COMPANY_API_BASE}/${updateCompany.id}`, updateCompany, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteCompanyById = async (id: number) => {
    await api.delete(`${COMPANY_API_BASE}/${id}`, getAuthConfig());
}