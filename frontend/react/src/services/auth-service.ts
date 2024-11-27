import axios from 'axios'
import {LoginRequest, NewPasswordPutRequest, PasswordResetRequest, RegisterRequest} from "@/types/user-types.ts";


const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

export const login = async (loginRequest: LoginRequest) => {
    try {
        return await api.post('/api/auth/login', loginRequest);
    } catch (e: any) {
        throw e;
    }
};

export const registerUser = async (registerRequest: RegisterRequest) => {
    try {
        return await api.post('/api/auth/register', registerRequest);
    } catch (e: any) {
        throw e;
    }
};
export const confirmAccount = async (token: string) => {
    try {
        return await api.get(`/api/auth/confirm?token=${token}`);
    } catch (e: any) {
        throw e;
    }
};


export const requestPasswordReset = async (passwordResetRequest: PasswordResetRequest) => {
    try {
        return await api.put('/api/auth/request-password-reset', passwordResetRequest);
    } catch (e: any) {
        throw e;
    }
};

export const resetPassword = async (newPasswordPutRequest: NewPasswordPutRequest) => {
    try {
        return await api.put('/api/auth/reset-password', newPasswordPutRequest);
    } catch (e: any) {
        throw e;
    }
};