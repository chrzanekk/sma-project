import i18n from "i18next";
import React from "react";


export const formatMessage = (
    code: string,
    details?: Record<string, any>,
    namespace: string = "common"
    ): React.ReactNode => {

    const translatedMessage = i18n.t(`${namespace}:${code}`, {
        ...details,
        defaultValue: i18n.t("common:generic"),
    });

    if (details) {
        Object.keys(details).forEach((key) => {
            const value = details[key];
            if (typeof value === "string") {
                details[key] = `"${value}"`;
            }
        });
    }

    return i18n.t(`${namespace}:${code}`, {
        ...details,
        defaultValue: translatedMessage,
    });

};
