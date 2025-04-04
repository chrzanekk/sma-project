// src/contexts/CompanyContext.tsx
import React, {createContext, useEffect, useState} from "react";
import {CompanyBaseDTO} from "@/types/company-type";

// Typ kontekstu
interface CompanyContextType {
    selectedCompany: CompanyBaseDTO | null;
    setSelectedCompany: (company: CompanyBaseDTO | null) => void;
}

export const CompanyContext = createContext<CompanyContextType | undefined>(undefined);

// Provider
export const CompanyProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [selectedCompany, setSelectedCompanyState] = useState<CompanyBaseDTO | null>(null);

    useEffect(() => {
        const storedCompany = localStorage.getItem("selectedCompany");
        if (storedCompany) {
            setSelectedCompanyState(JSON.parse(storedCompany));
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

    return (
        <CompanyContext.Provider value={{selectedCompany, setSelectedCompany}}>
            {children}
        </CompanyContext.Provider>
    );
};
