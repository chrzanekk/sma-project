import i18n from "i18next";

export const formatMessage = (
    code: string | undefined,
    details?: Record<string, any>,
    namespace: string = "common",
    backendFallbackMessage?: string
): string => {

    // 1. Zabezpieczenie przed brakiem kodu błędu
    if (!code) {
        return backendFallbackMessage || i18n.t("common:generic");
    }

    // 2. Kopiujemy obiekt details, aby nie mutować oryginału
    const formattedDetails = details ? { ...details } : undefined;
    if (formattedDetails) {
        Object.keys(formattedDetails).forEach((key) => {
            const value = formattedDetails[key];
            if (typeof value === "string") {
                formattedDetails[key] = `"${value}"`; // Dodanie cudzysłowów tylko w kopii
            }
        });
    }

    // 3. Fallback: Jeśli nie ma tłumaczenia, użyj wiadomości z backendu.
    // Jeśli z backendu też nic nie przyszło, użyj komunikatu generycznego.
    const fallback = backendFallbackMessage || i18n.t("common:generic");

    // 4. Jedno właściwe wywołanie tłumaczenia
    return i18n.t(`${namespace}:${code}`, {
        ...formattedDetails,
        defaultValue: fallback,
    });
};