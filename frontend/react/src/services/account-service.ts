import axios from "axios";
import {UserInfo, UserEditPasswordChangeRequest, UserEditRoleUpdateRequest, UserUpdateRequest} from "@/types/user-types.ts";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

const ACCOUNT_API_BASE = "/api/account";


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

export const getUserInfo = async (): Promise<UserInfo> => {
    try {
        const response = await api.get(`${ACCOUNT_API_BASE}/get`,
            getAuthConfig())
        return response.data;
    } catch (error: any) {
        throw error;
    }
}

export const updateUserAccount = async (user: UserUpdateRequest): Promise<void> => {
    try {
        await api.put(`${ACCOUNT_API_BASE}/update`, user, getAuthConfig());
    } catch (error: any) {
        throw error;
    }
};

export const changeUserPassword = async (userPasswordChangeRequest: UserEditPasswordChangeRequest): Promise<void> => {
    try {
        await api.put<boolean>(
            `${ACCOUNT_API_BASE}/change-password`,
            userPasswordChangeRequest,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};

export const updateUserRoles = async (userRoleUpdateRequest: UserEditRoleUpdateRequest): Promise<void> => {
    try {
        await api.put<boolean>(
            `${ACCOUNT_API_BASE}/update-roles`,
            userRoleUpdateRequest,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};