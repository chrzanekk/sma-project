import {BaseContractorDTO} from "@/types/contractor-types.ts";
import {AuditableType} from "@/types/auditable-type.ts";

export interface FetchableContactDTO extends ContactDTO, AuditableType {
}

export interface ContactDTO extends BaseContactDTOForContractor {
    contractors?: Array<BaseContractorDTO>
}

export interface BaseContactDTOForContractor {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}

export interface BaseContactFormValues {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}
