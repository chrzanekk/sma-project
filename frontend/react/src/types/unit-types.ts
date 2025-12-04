import {CompanyBaseDTO} from "@/types/company-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";
import {UnitType} from "@/types/enums/unit-types-enum.ts";

export interface FetchableUnitDTO extends UnitDTO, AuditableTypes {

}


export interface UnitDTO extends UnitBaseDTO {
    company: CompanyBaseDTO;
}

export interface UnitBaseDTO {
    id?: number;
    symbol: string;
    description?: string;
    unitType: UnitType;

}

export interface BaseUnitFormValues {
    id?: number;
    symbol: string;
    description?: string;
    unitType: string;
}