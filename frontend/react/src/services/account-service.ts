import axios from "axios";
import {User, UserInfo, UserPasswordChangeRequest} from "@/types/user-types.ts";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("auth")}`
    }
})

export const getUserInfo = async (): Promise<UserInfo> => {
    try {
        const response = await api.get(`api/account/get`,
            getAuthConfig())
        return response.data;
    } catch (error) {
        console.error("Błąd pobierania informacji o użytkowniku:", error);
        //todo add translation for error here
        throw error;
    }
}

export const updateUserAccount = async (user: User): Promise<void> => {
    try {
        await api.put("/api/account/update", user, getAuthConfig());
    } catch (error: any) {
        console.error("Błąd aktualizacji konta użytkownika:", error);
        throw error;
    }
};

export const changeUserPassword = async (userPasswordChangeRequest: UserPasswordChangeRequest): Promise<boolean> => {
    try {
        const response = await api.put<boolean>(
            "/api/account/change-password",
            userPasswordChangeRequest,
            getAuthConfig()
        );
        return response.data;
    } catch (error: any) {
        console.error("Błąd zmiany hasła użytkownika:", error);
        throw error;
    }
};