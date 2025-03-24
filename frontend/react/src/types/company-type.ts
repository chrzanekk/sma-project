import {AuditableType} from "@/types/auditable-type.ts";

export interface CompanyDTO extends CompanyBaseDTO, AuditableType {
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
