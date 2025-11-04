import {CompanyBaseDTO} from "@/types/company-types.ts";
import {PositionBaseDTO} from "@/types/position-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";


export interface FetchableEmployeeDTO extends EmployeeDTO, AuditableTypes {

}

export interface EmployeeDTO extends EmployeeBaseDTO {
    company: CompanyBaseDTO;
    position: PositionBaseDTO;
}


export interface EmployeeBaseDTO {
    id?: number;
    firstName: string;
    lastName: string;
    hourRate: string;

}

export interface BaseEmployeeFormValues {
    id?: number;
    firstName: string;
    lastName: string;
    hourRate: string;
}