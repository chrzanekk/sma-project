import {BaseContactDTOForContractor} from "@/types/contact-types.ts";
import {Country} from "@/types/country-type.ts";


//interfejs do wyswietlania kontrahentów wraz z kontaktami
export interface FetchableContractorDTO {
    id?: number;
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: Country;
    customer: boolean;
    supplier: boolean;
    scaffoldingUser: boolean;
    contacts: Array<BaseContactDTOForContractor>
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

//podstawowy interfejs użyty jako typ do wyświetlania kontaktó z kontrahentami (by uniknąć odwołania cyklicznego)
export interface BaseContractorDTOForContact {
id?: number;
name: string;
taxNumber: string;
street: string;
buildingNo: string;
apartmentNo: string;
postalCode: string;
city: string;
country: Country;
customer: boolean;
supplier: boolean;
scaffoldingUser: boolean;
}

//interfejs do dodania kontrahenta wraz z kontaktami (lub pusta lista jeśli user nie dodał nic)
export interface ContractorDTO {
    id?: number;
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: Country;
    customer: boolean;
    supplier: boolean;
    scaffoldingUser: boolean;
    contacts?: Array<BaseContactDTOForContractor>;
}

//interfejs używany w formularzach - country jako string - mapowany na enum
export interface ContractorFormValues {
    id?: number;
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: string;
    customer: boolean | undefined;
    supplier: boolean | undefined;
    scaffoldingUser: boolean | undefined;
    contacts?: Array<BaseContactDTOForContractor>;
}
