import {jwtDecode} from "jwt-decode";
import {errorNotification} from "@/notifications/notifications.ts";
import {JwtPayload} from "@/types/jwt-payload.ts";

export const isTokenExpired = (token: string): boolean => {
    try {
        const decoded: JwtPayload = jwtDecode(token);
        const currentTime = Math.floor(Date.now() / 1000);
        return decoded.exp < currentTime;
    } catch (error) {
        console.error("Błąd dekodowania tokena:", error);
        errorNotification("Błąd tokena", "Nieprawidłowy token uwierzytelniający.");
        return true;
    }
};