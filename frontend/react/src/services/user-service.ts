import {AdminEditPasswordChangeRequest, UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {RoleDTO} from "@/types/role-types.ts";
import {api, getAuthConfig, toBoolean} from "@/services/axios-config.ts";

const USERS_API_BASE = "/api/users";

export const getUsersByFilter = async (filter: Record<string, any>) => {
    try {
        const response = await api.get(`${USERS_API_BASE}/page`,
            {params: {...filter, size: filter.size || 10, page: filter.page || 0}, ...getAuthConfig(),});
        const data = response.data;
        return {
            users: data || [],
            totalPages: data.totalPages || 1
        }
    } catch (err) {
        console.error('Error fetching users: ', err);
        return {users: [], totalPages: 1};
    }
};

export const getUserById = async (id: number) => {
    try {
        const response = await api.get(`${USERS_API_BASE}/getById/${id}`, getAuthConfig());
        const userDTO: UserDTO = response.data;
        return userDTO;
    } catch (err) {
        console.error('Error fetching user by id:', err)
        throw err;
    }
}

export const addUser = async (addUser: UserFormDTO) => {
    console.log('AddUser', addUser)
    try {
        const roles: Array<RoleDTO> = Array.from(
            addUser.roles.map((role: string) => ({name: role}))
        );
        const payload: UserDTO = {
            ...addUser,
            locked: toBoolean(addUser.locked),
            enabled: toBoolean(addUser.enabled),
            roles
        }
        console.log('Payload', payload)
        const response = await api.post(`${USERS_API_BASE}/add`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        console.error('Error adding user:', err);
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
            locked: toBoolean(user.locked),
            enabled: toBoolean(user.enabled),
            roles
        }
        const response = await api.put(`${USERS_API_BASE}/update`, userDTO, getAuthConfig());
        return response.data;
    } catch (err) {
        console.error('Error updating user', err)
        throw err;
    }
};

export const deleteUserById = async (id: number) => {
    await api.delete(`${USERS_API_BASE}/delete/${id}`, getAuthConfig());
};

export const setNewUserPassword = async (setNewPassword: AdminEditPasswordChangeRequest): Promise<void> => {
    try {
        console.log("Sending request with payload:", setNewPassword);
        await api.put<boolean>(
            `${USERS_API_BASE}/set-new-password`,
            setNewPassword,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};