import {ContactBaseDTO} from "@/types/contact-types.ts";
import {Country} from "@/types/country-type.ts";

export interface ContractorBaseDTO {
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
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}


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
    contacts: Array<ContactBaseDTO>
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

export interface AddContractorDTO {
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

export interface AddContractorFormValues {
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: string;
    customer: boolean;
    supplier: boolean;
    scaffoldingUser: boolean;
}

export interface EditContractorDTO {
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

export interface EditContractorFormValues {
    id?: number;
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: string;
    customer: boolean;
    supplier: boolean;
    scaffoldingUser: boolean;
}