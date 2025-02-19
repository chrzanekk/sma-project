import {ContractorBaseDTO} from "@/types/contractor-types.ts";

export interface ContactBaseDTO {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

export interface ContactDTO {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
    contractors?: Array<ContractorBaseDTO>
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

export interface AddContactFormValues {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}

export interface AddContactDTO {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}
export interface EditContactFormValues {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}

export interface EditContactDTO {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}