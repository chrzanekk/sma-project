import axios from "axios";
import {errorNotification} from "@/notifications/notifications.ts";
import i18n from "i18next";
import {formatMessage} from "@/notifications/FormatMessage.tsx";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 10000,
});

const getAuthConfig = () => {
    const token = localStorage.getItem("auth");
    if (!token) {
        console.error(i18n.t('errors:jwtTokenNotAvailable'));
    }
    return {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    };
};


api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Przekieruj na stronę główną
            window.location.href = '/';
            // Pokaż komunikat o wygasłej sesji

            // Wyczyść token z localStorage
            localStorage.removeItem("auth");

            // WAŻNE: Zatrzymaj dalsze wykonywanie
            return Promise.reject({
                code: "sessionExpired",
                message: i18n.t('errors:sessionExpiredMessage'),
                status: 401,
            });
        }
        if (error.response) {
            // WYCIĄGAMY 'message' z JSON-a
            const {code, details, message} = error.response.data || {};

            // PRZEKAZUJEMY 'message' jako 4. parametr
            const formatedMessage = formatMessage(code, details, 'errors', message);

            errorNotification(i18n.t('errors:error'), formatedMessage);

            return Promise.reject({
                code,
                message: formatedMessage,
                details,
                status: error.response.status,
            });
        }

        errorNotification(
            i18n.t('errors:networkError'),
            i18n.t('errors:cannotConnectToServer')
        );

        return Promise.reject({
            code: "networkError",
            message: i18n.t('errors:cannotConnectToServer'),
            details: null,
            status: null,
        });
    }
);

export {api, getAuthConfig};
