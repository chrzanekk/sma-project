import {ScaffoldingLogFilter} from "@/filters/scaffolding-log-filter.ts";
import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {FetchableScaffoldingLogDTO, ScaffoldingLogDTO} from "@/types/scaffolding-log-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";
import {ApiPath} from "@/constants/api-paths.ts";


export const getScaffoldingLogByFilter = async (filter: ScaffoldingLogFilter) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${ApiPath.SCAFFOLDING_LOG}?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            logs: items as FetchableScaffoldingLogDTO[],
            totalPages
        };
    } catch (error) {
        return {logs: [], totalPages: 1}
    }
}

export const getScaffoldingLogById = async (id: number) => {
    const response = await api.get(`${ApiPath.SCAFFOLDING_LOG}/${id}`, getAuthConfig());
    const logDTO: FetchableScaffoldingLogDTO = response.data;
    return logDTO;
}

export const addScaffoldingLog = async (addLog: ScaffoldingLogDTO) => {
    const company: CompanyBaseDTO | null = getSelectedCompany();
    const payload = {
        ...addLog,
        company
    }
    const response = await api.post(`${ApiPath.SCAFFOLDING_LOG}`, payload, getAuthConfig());
    return response.data;
}

export const updateScaffoldingLog = async (updateLog: ScaffoldingLogDTO) => {
    const company: CompanyBaseDTO | null = getSelectedCompany();
    const payload = {
        ...updateLog,
        company
    }
    const response = await api.put(`${ApiPath.SCAFFOLDING_LOG}/${payload.id}`, payload, getAuthConfig());
    return response.data;

}


export const deleteScaffoldingLog = async (id: number) => {
    await api.delete(`${ApiPath.SCAFFOLDING_LOG}/${id}`, getAuthConfig());
}