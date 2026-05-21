// src/types/technical-protocol-types.ts

export interface TechnicalProtocolData {
    // Automatycznie z pozycji
    scaffoldingNumber: string;
    companyName: string;
    contractorName: string;
    contractorContactFirstName: string;
    contractorContactLastName: string;
    scaffoldingUserName: string;
    scaffoldingUserContactFirstName: string;
    scaffoldingUserContactLastName: string;
    assemblyLocation: string;
    assemblyDate: string;
    dimensions: string; // Sformatowane wymiary (np. "10m x 5m x 3m")

    // Pola do uzupełnienia w komponencie
    scaffoldingPurpose?: string; // Nr 5 - cel/przeznaczenie
    loadLimit?: string; // Nr 6 - domyślnie "2,0 kN/m²"
    earthingResistance?: string; // Nr 7 - uziemienie
    additionalInfo?: string; // Nr 10 - dodatkowe informacje
}
