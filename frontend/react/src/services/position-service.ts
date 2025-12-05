import {PositionFilter} from "@/filters/position-filter.ts";
import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {FetchablePositionDTO, PositionDTO} from "@/types/position-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";

const POSITION_API_BASE = "/api/positions";

export const getPositionByFilter = async (filter: PositionFilter) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${POSITION_API_BASE}?${queryParams}`, getAuthConfig())
        const {items, totalPages} = parsePaginationResponse(response)
        return {
            positions: items as FetchablePositionDTO[],
            totalPages
        };
    } catch (error) {
        return {positions: [], totalPages: 1};
    }
}

export const getPositionById = async (id: number) => {
    try {
        const response = await api.get(`${POSITION_API_BASE}/${id}`, getAuthConfig())
        const positionDTO: FetchablePositionDTO = response.data;
        return positionDTO;
    } catch (error) {
        throw error;
    }
}

export const addPosition = async (addPosition: PositionDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addPosition,
            company
        }
        const response = await api.post(`${POSITION_API_BASE}`, payload, getAuthConfig());
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const updatePosition = async (updatePosition: PositionDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updatePosition,
            company
        }
        const response = await api.put(`${POSITION_API_BASE}/${updatePosition.id}`, payload, getAuthConfig());
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const deletePositonById = async (id: number) => {
    await api.delete(`${POSITION_API_BASE}/${id}`, getAuthConfig())
}