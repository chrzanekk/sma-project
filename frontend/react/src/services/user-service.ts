import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

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

export const getAllUsers = async () => {
    const response = await api.get(`${USERS_API_BASE}/all`, getAuthConfig());
    return response.data;
};

export const getUsersByFilter = async (filter: Record<string, any>) => {
    const response = await api.get(`${USERS_API_BASE}/`, {params: filter, ...getAuthConfig()});
    return response.data;
};

export const addUser = async (user: Record<string, any>) => {
    const response = await api.post(`${USERS_API_BASE}/add`, user, getAuthConfig());
    return response.data;
};

export const updateUser = async (user: Record<string, any>) => {
    const response = await api.put(`${USERS_API_BASE}/update`, user, getAuthConfig());
    return response.data;
};

export const deleteUserById = async (id: number) => {
    await api.delete(`${USERS_API_BASE}/delete/${id}`, getAuthConfig());
};