// src/contexts/CompanyContext.tsx
import React, {createContext, useEffect, useState} from "react";
import {CompanyBaseDTO} from "@/types/company-types.ts";

// Typ kontekstu
interface CompanyContextType {
    selectedCompany: CompanyBaseDTO | null;
    setSelectedCompany: (company: CompanyBaseDTO | null) => void;
    resetSelectedCompany: () => void;
}

export const CompanyContext = createContext<CompanyContextType | undefined>(undefined);

// Provider
export const CompanyProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [selectedCompany, setSelectedCompanyState] = useState<CompanyBaseDTO | null>(null);

    useEffect(() => {
        const storedCompany = localStorage.getItem("selectedCompany");
        if (storedCompany && storedCompany !== "null") {
            try{
                const parsed = JSON.parse(storedCompany);
                if (parsed && typeof parsed === 'object' && parsed.id) {
                    setSelectedCompanyState(parsed)
                }
            } catch (e) {
                console.warn("Niepoprawny obiekt firmy w localStorage:", e);
            }

        }
    }, []);

    const setSelectedCompany = (company: CompanyBaseDTO | null) => {
        setSelectedCompanyState(company);
        if (company) {
            localStorage.setItem("selectedCompany", JSON.stringify(company));
        } else {
            localStorage.removeItem("selectedCompany");
        }
    };

    const resetSelectedCompany = () => {
        setSelectedCompany(null);
        localStorage.removeItem("companySelected");
    };

    return (
        <CompanyContext.Provider value={{selectedCompany, setSelectedCompany, resetSelectedCompany}}>
            {children}
        </CompanyContext.Provider>
    );
};
