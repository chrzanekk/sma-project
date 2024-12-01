import axios from "axios";
import {UserInfo, UserPasswordChangeRequest, UserRoleUpdateRequest, UserUpdateRequest} from "@/types/user-types.ts";

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

export const getUserInfo = async (): Promise<UserInfo> => {
    try {
        const response = await api.get(`api/account/get`,
            getAuthConfig())
        return response.data;
    } catch (error: any) {
        throw error;
    }
}

export const updateUserAccount = async (user: UserUpdateRequest): Promise<void> => {
    try {
        await api.put("/api/account/update", user, getAuthConfig());
    } catch (error: any) {
        throw error;
    }
};

export const changeUserPassword = async (userPasswordChangeRequest: UserPasswordChangeRequest): Promise<void> => {
    try {
        await api.put<boolean>(
            "/api/account/change-password",
            userPasswordChangeRequest,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};

export const updateUserRoles = async (userRoleUpdateRequest: UserRoleUpdateRequest): Promise<void> => {
    try {
        await api.put<boolean>(
            "/api/account/update-roles",
            userRoleUpdateRequest,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};