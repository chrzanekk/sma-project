import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {
    ConstructionSiteCreateDTO,
    ConstructionSiteDTO,
    ConstructionSiteUpdateDTO,
    FetchableConstructionSiteDTO
} from "@/types/constrution-site-types.ts";
import {CompanyBaseDTO} from "@/types/company-type.ts";
import {ContractorDTO} from "@/types/contractor-types.ts";


const CONSTRUCTION_SITE_API_BASE = "/api/construction-sites";

export const getConstructionSiteByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            countryCode: filter.country || '',
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0,
            sort: filter.sort || 'id,asc'
        });
        const response = await api.get(`${CONSTRUCTION_SITE_API_BASE}?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            constructionSites: items as FetchableConstructionSiteDTO[],
            totalPages
        }
    } catch (err) {
        return {constructionSites: [], totalPages: 1};
    }
}

export const getConstructionSiteById = async (id: number) => {
    try {
        const response = await api.get(`${CONSTRUCTION_SITE_API_BASE}/${id}`, getAuthConfig());
        const constructionSiteDTO: FetchableConstructionSiteDTO = response.data;
        return constructionSiteDTO;
    } catch (err) {
        throw err;
    }
}

export const getContractorsByConstructionSiteIdPaged = async (
    constructionSiteId: number,
    page: number,
    size: number,
) => {
    const response = await api.get(`${CONSTRUCTION_SITE_API_BASE}/constructionSite/${constructionSiteId}/contractors`, {
        params: {
            constructionSiteId,
            page,
            size,
            sort: 'id,asc'
        },
        ...getAuthConfig(),
    });
    return parsePaginationResponse<ContractorDTO>(response);
}

export const addConstructionSite = async (addConstructionSite: ConstructionSiteDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addConstructionSite,
            company
        }
        const response = await api.post(`${CONSTRUCTION_SITE_API_BASE}`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const createConstructionSite = async (createConstructionSite: ConstructionSiteCreateDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...createConstructionSite,
            company
        };
        const response = await api.post(`${CONSTRUCTION_SITE_API_BASE}/create`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const updateConstructionSite = async (updateConstructionSite: ConstructionSiteDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateConstructionSite,
            company
        };
        const response = await api.put(`${CONSTRUCTION_SITE_API_BASE}/${updateConstructionSite.id}`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const extendedUpdateConstructionSite = async (updateConstructionSite: ConstructionSiteUpdateDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateConstructionSite,
            company
        };
        const response = await api.put(`${CONSTRUCTION_SITE_API_BASE}/extended-update`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteConstructionSiteById = async (id: number) => {
    await api.delete(`${CONSTRUCTION_SITE_API_BASE}/${id}`, getAuthConfig());
}