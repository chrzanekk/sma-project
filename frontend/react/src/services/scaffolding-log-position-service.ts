import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";
import {ApiPath} from "@/constants/api-paths.ts";
import {FetchableScaffoldingLogPositionDTO, ScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";
import {ScaffoldingLogPositionFilter} from "@/filters/scaffolding-log-position-filter.ts";


export const getScaffoldingLogPositionByFilter = async (filter: ScaffoldingLogPositionFilter) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            scaffoldingLogId: filter.scaffoldingLogId,
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${ApiPath.SCAFFOLDING_LOG_POSITION}?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            logs: items as FetchableScaffoldingLogPositionDTO[],
            totalPages
        };
    } catch (error) {
        return {logs: [], totalPages: 1}
    }
}

export const getScaffoldingLogPositionById = async (id: number) => {
    const response = await api.get(`${ApiPath.SCAFFOLDING_LOG_POSITION}/${id}`, getAuthConfig());
    const logDTO: FetchableScaffoldingLogPositionDTO = response.data;
    return logDTO;
}

export const addScaffoldingLogPosition = async (addLog: ScaffoldingLogPositionDTO) => {
    const company: CompanyBaseDTO | null = getSelectedCompany();
    const payload = {
        ...addLog,
        company
    }
    const response = await api.post(`${ApiPath.SCAFFOLDING_LOG_POSITION}`, payload, getAuthConfig());
    return response.data;
}

export const updateScaffoldingLogPosition = async (updateLog: ScaffoldingLogPositionDTO) => {
    const company: CompanyBaseDTO | null = getSelectedCompany();
    const payload = {
        ...updateLog,
        company
    }
    const response = await api.put(`${ApiPath.SCAFFOLDING_LOG_POSITION}/${payload.id}`, payload, getAuthConfig());
    return response.data;
}

export const deleteScaffoldingLogPosition = async (id: number) => {
    await api.delete(`${ApiPath.SCAFFOLDING_LOG_POSITION}/${id}`, getAuthConfig());
}