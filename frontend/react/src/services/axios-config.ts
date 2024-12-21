import axios from "axios";
import {errorNotification} from "@/notifications/notifications.ts";
import i18n from "i18next";
import {formatMessage} from "@/notifications/FormatMessage.tsx";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 10000,
});

const toBoolean = (value: string | boolean): boolean => value === "true" || value === true;

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
        if (error.response) {
            const {code, details} = error.response.data || {};
            const formatedMessage = formatMessage(code, details, 'errors');

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

export {api, getAuthConfig, toBoolean};
