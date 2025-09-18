import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";

export interface FetchableContactDTO extends ContactDTO, AuditableTypes {
}

export interface ContactDTO extends BaseContactDTOForContractor {
    contractor?: ContractorBaseDTO
}

export interface BaseContactDTOForContractor {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
    company: CompanyBaseDTO;
}

export interface BaseContactFormValues {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}
