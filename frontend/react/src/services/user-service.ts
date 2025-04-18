import {AdminEditPasswordChangeRequest, UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {RoleDTO} from "@/types/role-types.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";

const USERS_API_BASE = "/api/users";

export const getUsersByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            size: filter.size || 10,
            page: filter.page || 0,
            sort: filter.sort || 'id,asc'
        });
        const response = await api.get(`${USERS_API_BASE}/page?${queryParams}`,
            getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            users: items as UserDTO[],
            totalPages,
        };
    } catch (err) {
        return {users: [], totalPages: 1};
    }
};

export const getUserById = async (id: number) => {
    try {
        const response = await api.get(`${USERS_API_BASE}/getById/${id}`, getAuthConfig());
        const userDTO: UserDTO = response.data;
        console.table(userDTO)
        return userDTO;
    } catch (err) {
        throw err;
    }
}

export const addUser = async (addUser: UserFormDTO) => {
    try {
        const roles: Array<RoleDTO> = Array.from(
            addUser.roles.map((role: string) => ({name: role}))
        );
        const payload: UserDTO = {
            ...addUser,
            // locked: toBoolean(addUser.locked),
            // enabled: toBoolean(addUser.enabled),
            roles
        }
        const response = await api.post(`${USERS_API_BASE}/add`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
};

export const updateUser = async (user: UserFormDTO) => {
    try {
        const roles: Array<RoleDTO> = Array.from(
            user.roles.map((role: string) => ({name: role}))
        );
        const userDTO: UserDTO = {
            ...user,
            roles
        }
        const response = await api.put(`${USERS_API_BASE}/update`, userDTO, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
};

export const deleteUserById = async (id: number) => {
    await api.delete(`${USERS_API_BASE}/delete/${id}`, getAuthConfig());
};

export const setNewUserPassword = async (setNewPassword: AdminEditPasswordChangeRequest): Promise<void> => {
    try {
        await api.put<boolean>(
            `${USERS_API_BASE}/set-new-password`,
            setNewPassword,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};