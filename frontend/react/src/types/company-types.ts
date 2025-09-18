import {AuditableTypes} from "@/types/auditable-types.ts";

export interface FetchableCompanyDTO extends CompanyBaseDTO, AuditableTypes {
}

export interface CompanyBaseDTO {
    id?: number;
    name: string;
    additionalInfo: string;
}

export interface CompanyFormValues {
    id?: number;
    name: string;
    additionalInfo: string;
}
