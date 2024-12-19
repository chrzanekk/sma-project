import axios from "axios";
import {AddUserDTO, UserDTO} from "@/types/user-types.ts";
import {RoleDTO} from "@/types/role-types.ts";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

const toBoolean = (value: string | boolean): boolean => value === "true" || value === true;

const getAuthConfig = () => {

    const token = localStorage.getItem("auth");
    if (!token) {
        console.error("Token JWT nie jest dostępny w localStorage");
    }
    return {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    };
};

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

export const addUser = async (addUser: AddUserDTO) => {
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

export const updateUser = async (user: Record<string, any>) => {
    const response = await api.put(`${USERS_API_BASE}/update`, user, getAuthConfig());
    return response.data;
};

export const deleteUserById = async (id: number) => {
    await api.delete(`${USERS_API_BASE}/delete/${id}`, getAuthConfig());
};