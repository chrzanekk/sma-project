import axios from 'axios'
import {LoginRequest, NewPasswordPutRequest, PasswordResetRequest, RegisterRequest} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";
import {errorNotification} from "@/notifications/notifications.ts";


const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

export const login = async (loginRequest: LoginRequest, t: any) => {
    try {
        return await api.post('/api/auth/login', loginRequest);
    } catch (e: any) {
        errorNotification(t('error.loginFailed'), e.response?.data?.message || t('error.generic'));
        throw e;
    }
};

export const registerUser = async (registerRequest: RegisterRequest) => {
    try {
        return await api.post('/api/auth/register', registerRequest);
    } catch (e: any) {
        const {t} = useTranslation();
        errorNotification(t('error.registerFailed'), e.response?.data?.message || t('error.generic'));
        throw e;
    }
};
export const confirmAccount = async (token: string) => {
    try {
        return await api.get(`/api/auth/confirm?token=${token}`);
    } catch (e: any) {
        const { t } = useTranslation();
        errorNotification(t('error.confirmationFailed'), e.response?.data?.message || t('error.generic'));
        throw e;
    }
};


export const requestPasswordReset = async (passwordResetRequest: PasswordResetRequest) => {
    try {
        return await api.put('/api/auth/request-password-reset', passwordResetRequest);
    } catch (e: any) {
        const {t} = useTranslation();
        errorNotification(t('error.requestPasswordResetFailed'), e.response?.data?.message || t('error.generic'));
        throw e;
    }
};

export const resetPassword = async (newPasswordPutRequest: NewPasswordPutRequest) => {
    try {
        return await api.put('/api/auth/reset-password', newPasswordPutRequest);
    } catch (e: any) {
        const {t} = useTranslation();
        errorNotification(t('error.passwordResetFailed'), e.response?.data?.message || t('error.generic'));
        throw e;
    }
};