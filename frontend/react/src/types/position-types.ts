import {CompanyBaseDTO} from "@/types/company-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";


export interface FetchablePositionDTO extends PositionDTO, AuditableTypes {
}


export interface PositionDTO extends PositionBaseDTO {
    company: CompanyBaseDTO;
}


export interface PositionBaseDTO {
    id?: number;
    name: string;
    description: string;
}

export interface BasePositionFormValues {
    id?: number;
    name: string;
    description: string;
}