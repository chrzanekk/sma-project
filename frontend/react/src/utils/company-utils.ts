// Funkcja pomocnicza do pobierania aktualnie wybranego companyId z localStorage
import {CompanyBaseDTO} from "@/types/company-types.ts";

export const getSelectedCompanyId = (): number | null => {
    const storedCompany = localStorage.getItem("selectedCompany");
    if (storedCompany) {
        try {
            const company = JSON.parse(storedCompany);
            return company.id;
        } catch (error) {
            console.error("Błąd parsowania selectedCompany z localStorage:", error);
            return null;
        }
    }
    return null;
};


export const getSelectedCompany = (): CompanyBaseDTO | null => {
    const storedCompany = localStorage.getItem("selectedCompany");
    if (storedCompany) {
        try {
            return JSON.parse(storedCompany);
        } catch (error) {
            console.error("Błąd parsowania selectedCompany z localStorage:", error);
            return null;
        }
    }
    return null;
};
