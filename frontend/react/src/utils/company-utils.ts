// Funkcja pomocnicza do pobierania aktualnie wybranego companyId z localStorage
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
