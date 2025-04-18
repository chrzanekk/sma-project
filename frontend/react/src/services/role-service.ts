import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {RoleDTO} from "@/types/role-types.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";

const API_ROLES_BASE = "/api/roles"

export const getAllRoles = async () => {
    try {
        const response = await api.get(`${API_ROLES_BASE}/all`, getAuthConfig());
        const data = response.data;
        return data || [];
    } catch (err) {
        return [];
    }
}

export const getRoleByFilterAndPage = async(filter: Record<string,any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            size: filter.size || 10,
            page: filter.page || 0,
            sort: filter.sort || 'id,asc'
        });
        const response = await api.get(`${API_ROLES_BASE}/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            roles: items as RoleDTO[],
            totalPages,
        };
    } catch (err) {
        return {roles: [], totalPages: 1};
    }
}

export const addNewRole = async (role: RoleDTO) => {
    try {
        const response = await api.post(`${API_ROLES_BASE}/save`, role, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteRoleById = async (id: number) => {
    try {
        const response = await api.delete(`${API_ROLES_BASE}/delete/${id}`, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}