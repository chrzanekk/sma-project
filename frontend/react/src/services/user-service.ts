import axios from "axios";
import {UserDTO} from "@/types/user-types.ts";

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

export const addUser = async (user: UserDTO) => {
    console.log('AddUser', user)
    try {
        const roles = user.roles?.map((role) => ({ name: role }));
        const payload = {
            ...user,
            locked: toBoolean(user.locked),
            enabled: toBoolean(user.enabled),
            roles
        }
        const response = await api.post(`${USERS_API_BASE}/add`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        console.error('Error adding user:', err);
    }

};

export const updateUser = async (user: Record<string, any>) => {
    const response = await api.put(`${USERS_API_BASE}/update`, user, getAuthConfig());
    return response.data;
};

export const deleteUserById = async (id: number) => {
    await api.delete(`${USERS_API_BASE}/delete/${id}`, getAuthConfig());
};