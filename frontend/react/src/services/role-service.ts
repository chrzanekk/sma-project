import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {RoleDTO} from "@/types/role-types.ts";

const API_ROLES_BASE = "/api/roles"

export const getAllRoles = async () => {
    try {
        const response = await api.get(`${API_ROLES_BASE}/all`, getAuthConfig());
        const data = response.data;
        return data || [];
    } catch (err) {
        console.error('Error fetching roles: ', err);
        return [];
    }
}

export const getRoleByFilterAndPage = async(filter: Record<string,any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            size: filter.size || 10,
            page: filter.page || 0,
        });
        const response = await api.get(`${API_ROLES_BASE}/page?${queryParams}`, getAuthConfig());
        const data = response.data;
        return {
            roles: data || [],
            totalPages: data.totalPages || 1
        }
    } catch (err) {
        console.error('Error fetching roles: ', err);
        return {roles: [], totalPages: 1};
    }
}

export const addNewRole = async (role: RoleDTO) => {
    try {
        const response = await api.post(`${API_ROLES_BASE}/save`, role, getAuthConfig());
        return response.data;
    } catch (err) {
        console.error('Error adding role:', err);
        throw err;
    }
}

export const deleteRoleById = async (id: number) => {
    try {
        const response = await api.delete(`${API_ROLES_BASE}/delete/${id}`, getAuthConfig());
        return response.data;
    } catch (err) {
        console.error('Error deleting role:', err);
        throw err;
    }
}