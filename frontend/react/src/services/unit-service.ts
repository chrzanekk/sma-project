import {UnitFilter} from "@/types/filters/unit-filter.ts";
import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {FetchableUnitDTO, UnitDTO} from "@/types/unit-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";

const UNIT_API_BASE = "/api/units";

export const getUnitsByFilter = async (filter: UnitFilter) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0,
        });
        const response = await api.get(`${UNIT_API_BASE}?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response)
        return {
            units: items as FetchableUnitDTO[],
            totalPages,
        };
    } catch (error) {
        return {units: [], totalPages: 1};
    }
}

export const getUnitById = async (id: number) => {
    try {
        const response = await api.get(`${UNIT_API_BASE}/${id}`, getAuthConfig());
        const unitDTO: FetchableUnitDTO = response.data as FetchableUnitDTO;
        return unitDTO;
    } catch (error) {
        throw error;
    }
}

export const addUnit = async (addUnit: UnitDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addUnit,
            company
        }
        const response = await api.post(`${UNIT_API_BASE}`, payload, getAuthConfig());
        return response.data;
    } catch (error) {
        throw error;
    }
}
export const updateUnit = async (id: number, updateUnit: UnitDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateUnit,
            company
        }
        const response = await api.put(`${UNIT_API_BASE}/${id}`, payload, getAuthConfig());
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const deleteUnit = async (id: number) => {
    await api.delete(`${UNIT_API_BASE}/${id}`, getAuthConfig());
}

