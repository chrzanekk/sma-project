import {BaseContractorDTO} from "@/types/contractor-types.ts";
import {AuditableType} from "@/types/auditable-type.ts";
import {CompanyBaseDTO} from "@/types/company-type.ts";

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
    companyId?: number;
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
