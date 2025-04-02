import axios from "axios";
import {
    AdminEditRoleUpdateRequest,
    RoleUpdateRequest,
    UserEditPasswordChangeRequest,
    UserInfo,
    UserUpdateRequest
} from "@/types/user-types.ts";
import {RoleDTO} from "@/types/role-types.ts";

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

export const updateUserRoles = async (userRoleUpdateRequest: AdminEditRoleUpdateRequest): Promise<void> => {
    const roles: Array<RoleDTO> = Array.from(
        userRoleUpdateRequest.roles.map((role: string) => ({name: role}))
    );
    const payload: RoleUpdateRequest = {
        userId: userRoleUpdateRequest.userId!,
        roles
    }
    try {
        await api.put<boolean>(
            `${ACCOUNT_API_BASE}/update-roles`,
            payload,
            getAuthConfig()
        );
    } catch (error: any) {
        throw error;
    }
};