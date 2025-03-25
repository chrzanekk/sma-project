import {AuditableType} from "@/types/auditable-type.ts";

export interface FetchableCompanyDTO extends CompanyBaseDTO, AuditableType {
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
