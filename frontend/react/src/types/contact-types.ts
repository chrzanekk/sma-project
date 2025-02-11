import {ContractorDTO} from "@/types/contractor-types.ts";

export interface ContactDTO {
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

export interface ContactHasContractorsDTO {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
    contractors?: Array<ContractorDTO>
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}