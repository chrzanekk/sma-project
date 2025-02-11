import {Country} from "@/types/country-type.ts";
import {ContactDTO} from "@/types/contact-types.ts";

export interface ContractorDTO {
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
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}


export interface ContractorHasContactsDTO {
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
    contacts: Array<ContactDTO>
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

export interface Contractor {
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
    createdDatetime: Date;
    lastModifiedDatetime: Date;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

export interface ContractorHasContacts {
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
    contacts: Array<ContactDTO>;
    createdDatetime: Date;
    lastModifiedDatetime: Date;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

export function mapToContractor(dto: ContractorDTO): Contractor {
    return {
        ...dto,
        country: Country.fromCode(dto.country),
        createdDatetime: new Date(dto.createdDatetime),
        lastModifiedDatetime: new Date(dto.lastModifiedDatetime),
    };
}

export function mapToContractorHasContacts(dto: ContractorHasContactsDTO): ContractorHasContacts {
    return {
        ...dto,
        country: Country.fromCode(dto.country),
        createdDatetime: new Date(dto.createdDatetime),
        lastModifiedDatetime: new Date(dto.lastModifiedDatetime),
    };
}

